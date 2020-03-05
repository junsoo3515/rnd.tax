package tax.www.interceptors;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import tax.www.service.cmn.CmnService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

/**
 * request 전처리
 * <p>
 * <p>
 * 이하 override 가능한 함수 목록
 * preHandle - Controller 실행 요청전 호출
 * postHandle - view 로 forward 되기 전 호출
 * afterCompletion - 처리가 끝난뒤 호출
 * <p>
 * User: 현재호
 * Date: 17. 10. 26
 * Time: 오후 4:50
 */
public class DefaultInterceptor extends HandlerInterceptorAdapter {

    private static final Logger log = LogManager.getLogger(DefaultInterceptor.class);

    // Session 환경설정 값
    @Value("#{cfg['login.url']}")
    private String loginUrlPath;

    @Resource(name = "CmnService")
    private CmnService cmnService;

    /**
     * request 전처리
     *
     * @param request  Http Request 객체
     * @param response Http Response 객체
     * @param handler  이벤트 핸들러
     * @return 성공/실패 여부
     * @throws java.io.IOException Redirection 경로를 찾지 못하는 경우
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        /*
         * 로직 구현시 이슈사항
         *
         * 1 Redirection 로직 구현
         * 1-1 response.encodeRedirectURL(loginUrlPath);
         *          기본적인 Redirection 로직
         *          하지만 해당 encodeRedirectURL 이후에 브라우저에서 세션, 쿠키, http 헤더 조작 로직이 있다면
         *          'Cannot create a session after the response has been committed' 에러가 발생함.
         * 1-2 request.getRequestDispatcher(loginUrlPath).forward(request, response) URL 주소가 변경이 안되기 때문에 여기에서는 사용 안함(URL 체크 들어감)
         *          강제 Redirection 로직, 브라우저에게 권한 없음.
         */

        try {

            HttpSession session = request.getSession();

            String urlPath = request.getRequestURI().substring(request.getContextPath().length()); // 2. 접근 URL의 Path 경로

//            log.debug(session.getAttribute("id") + ", " + session.getMaxInactiveInterval() + ", " + urlPath + " : " + request.getContextPath() + " : " + request.getHeader("X-Requested-With"));

            if (session.getAttribute("id") != null) {

                // 3. 세션 검사 (로그인 유무검사)
                if (StringUtils.isEmpty(session.getAttribute("token").toString()) == true) {

                    session.invalidate();
                    response.sendRedirect(response.encodeRedirectURL(loginUrlPath));

                    return false;
                }
            } else {
                // 클러스터링 지원을 위해 사용
                if (session.getAttribute("token") == null) {

                    session.invalidate();
                    response.sendRedirect(response.encodeRedirectURL(loginUrlPath));

                    return false;
                } else {

                    if (StringUtils.isEmpty(session.getAttribute("token").toString()) == true) {

                        session.invalidate();
                        response.sendRedirect(response.encodeRedirectURL(loginUrlPath));

                        return false;
                    }
                }
            }

            // 비정상적인 사용자 접근 체크 후 session 소멸 및 로그인 페이지 강제 이동(만약 이전 페이지가 존재하면 이전 페이지로 이동)
            if (request.getHeader("X-Requested-With") == null && (!StringUtils.startsWith(urlPath, "/upload") && !StringUtils.startsWith(urlPath, "/files") && !StringUtils.contains(urlPath, "Excel") && !StringUtils.endsWith(urlPath, "/popupVms"))) {
                // ajax로 보내지 않은 패턴 검색(접근 URL)
                try {

                    String redirectUrl = loginUrlPath;

                    switch (urlPath) {

                        case "/":
                        case "/tax/":

                            HashMap<String, String> firstMenuData = cmnService.getAccessFirstMenu(null, session.getAttribute("id").toString());

                            redirectUrl = String.format("%s%s", request.getContextPath(), firstMenuData.get("URL"));

                            firstMenuData = null;

                            if (StringUtils.equals(redirectUrl, loginUrlPath) == true) session.invalidate();

                            if (StringUtils.isEmpty(redirectUrl) == false) {

                                response.sendRedirect(response.encodeRedirectURL(redirectUrl));
                                return false;
                            }
                            break;
                        default:

                            switch (urlPath) {

                                case "/onepage/myinfo":
                                    // 우측상단 내 정보 메뉴(DB에 접근권한 관리하지 않는 메뉴)
                                    break;

                                case "/errors":
                                    // 서버 에러 발생 시
                                    break;

                                default:
                                    // TOMCAT에서 session 사용 시 처음 sessionid를 강제로 선언해서 사용하는 것 때문에 URL 찾아가는 버그 발생되어 강제 수정 함
                                    if (urlPath.contains("jsessionid")) {

                                        urlPath = urlPath.replace(";jsessionid=" + request.getRequestedSessionId(), "");

                                        response.sendRedirect(response.encodeRedirectURL(String.format("%s%s", request.getContextPath(), urlPath)));
                                    }

                                    if (cmnService.getAccessMenu(session.getAttribute("id").toString(), urlPath) == 0) {

                                        if (request.getHeader("referer") != null)
                                            redirectUrl = request.getHeader("referer").toString();

                                        if (StringUtils.equals(redirectUrl, loginUrlPath) == true) session.invalidate();

                                        response.sendRedirect(response.encodeRedirectURL(redirectUrl));
                                        return false;
                                    }
                                    break;
                            }

                            break;
                    }
                } catch (Exception ex) {

                    log.error(ex.toString(), ex);

                    session.invalidate();
                    response.sendRedirect(response.encodeRedirectURL(loginUrlPath));
                    return false;
                }
            }
        } catch (Exception ex) {

            log.error(ex.toString(), ex);
            return false;
        }

        // prehandle 함수내 로직의 성공/실패 유무 반환
        return true;
    }
}
