package tax.www.module.object;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * List/Map 타입 가공을 위한 추가 로직
 * <p/>
 * User: 현재호
 * Date: 17. 10. 26
 * Time: 오후 5:00
 */
@Component("CmnListBiz")
public class CmnListBiz {

    private static final Logger log = LogManager.getLogger(CmnListBiz.class);

    /**
     * 중복 값 삭제
     *
     * @param oriItem List<String> 인자 값
     * @return List<String>
     */
    public List<String> getUniqueList(List<String> oriItem) {

        // 중복 인자 삭제
        List<String> arrItems = new ArrayList<String>(new HashSet<String>(oriItem));

        return arrItems;
    }

    /**
     * List에 prefix 추가
     *
     * @param oriItem List<String> 인자 값
     * @return List<String>
     */
    public List<String> setPrefixList(List<String> oriItem, String str) {

        List<String> arrItems = new ArrayList<String>(oriItem);

        for (int i = 0; i < arrItems.size(); i++) {

            arrItems.set(i, str.concat(arrItems.get(i)));
        }

        return arrItems;
    }

    /**
     * Map Key 기준으로 정렬
     *
     * @param oriItem Map<String, Double> 인자 값
     * @param isDesc  내림차순 여부
     * @return TreeMap<String, Double>
     */
    public Map<String, Map<String, Object>> getSortKeyMap2(Map<String, Map<String, Object>> oriItem, boolean isDesc) {

        TreeMap<String, Map<String, Object>> tm = new TreeMap<String, Map<String, Object>>(oriItem);

        if (isDesc) {

            return tm.descendingMap();
        } else {

            return tm;
        }
    }

    /**
     * Map Key 기준으로 정렬
     *
     * @param oriItem Map<String, Double> 인자 값
     * @param isDesc  내림차순 여부
     * @return TreeMap<String, Double>
     */
    public Map<Double, Double> getSortKeyMap(Map<Double, Double> oriItem, boolean isDesc) {

        TreeMap<Double, Double> tm = new TreeMap<Double, Double>(oriItem);

        if (isDesc) {

            return tm.descendingMap();
        } else {

            return tm;
        }
    }

}
