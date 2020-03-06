package cc.domovoi.spring.controller.innerlayer.response;

public class InnerLayerAddResultResponse<K, E> {

    private Integer result;

    private K id;

    private E query;

    public InnerLayerAddResultResponse() {
    }

    public InnerLayerAddResultResponse(Integer result, K id, E query) {
        this.result = result;
        this.id = id;
        this.query = query;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public K getId() {
        return id;
    }

    public void setId(K id) {
        this.id = id;
    }

    public E getQuery() {
        return query;
    }

    public void setQuery(E query) {
        this.query = query;
    }
}
