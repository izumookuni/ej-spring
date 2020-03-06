package cc.domovoi.spring.controller.innerlayer.response;

public class InnerLayerUpdateDeleteResultResponse<E> {

    private Integer result;

    private E query;

    public InnerLayerUpdateDeleteResultResponse() {
    }

    public InnerLayerUpdateDeleteResultResponse(Integer result, E query) {
        this.result = result;
        this.query = query;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public E getQuery() {
        return query;
    }

    public void setQuery(E query) {
        this.query = query;
    }
}
