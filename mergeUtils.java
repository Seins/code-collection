public class MergeUtils{
    
    
    /**
     * 合并数据
     *
     * @param data
     * @return
     */
    private Map<String, List<List<String>>> merge(Map<String, List<String>> data) {
        Map<String, List<List<String>>> result = new HashMap<>();
        Set<String> keys = data.keySet();
        for (String key : keys) {
            if (MapUtils.isEmpty(result)) {
                List<List<String>> collection = new ArrayList<>();
                collection.add(data.get(key));
                result.put("Collection" + System.currentTimeMillis(), collection);
            } else {
                result = merge(data.get(key), result);
            }
        }
        return result;
    }

    /**
     * 在已有的集合中进行比对，如果发现有关联则并入该集合，否则，创建一个新的集合来进行串并
     *
     * @param item
     * @param data
     * @return
     */
    private Map<String, List<List<String>>> merge(List<String> item, Map<String, List<List<String>>> data) {
        Set<String> colKey = data.keySet();
        for (String key : colKey) {
            List<List<String>> cols = data.get(key);
            if (haveRelation(item, cols)) {
                cols.add(item);
                data.put(key, cols);
                break;
            } else {
                List<List<String>> newCols = new ArrayList<>();
                newCols.add(item);
                data.put("Collection" + System.currentTimeMillis(), newCols);
                LOGGER.info("未找到任何可关联的数据集合，创建新的集合！");
            }
        }
        return data;
    }

    /**
     * 将元素与集合碰撞，判断集合中的元素和该元素是否存在相似性
     *
     * @param item 元素
     * @param cols 集合
     * @return
     */
    private boolean haveRelation(List<String> item, List<List<String>> cols) {
        boolean haveRelation = false;
        long colIndex = 0;
        for (List<String> col : cols) {
            Set<String> collectionSet = new HashSet<>();
            long count = 0;
            collectionSet.addAll(col);
            for (String target : item) {
                if (collectionSet.contains(target)) {
                    count++;
                    haveRelation = true;
                }
            }
            if (haveRelation) {
                LOGGER.info("与第{}个集合因为{}个数据相同而被关联，结束当前数据的关系寻找！", colIndex, count);
                break;
            }
            colIndex++;
        }

        return haveRelation;
    }
}