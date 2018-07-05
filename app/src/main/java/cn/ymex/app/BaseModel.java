package cn.ymex.app;

/**
 * Created by ymexc on 2018/6/28.
 * About:TODO
 */
public class BaseModel<T> {
    private boolean error;
    private T results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public T getResults() {
        return results;
    }

    public void setResults(T results) {
        this.results = results;
    }
}
