package praktikumservices.stellar_burgers.entities;

import java.util.List;

public class IngredientsData {
    private boolean success;
    private List<Ingredients> data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }


    public List<Ingredients> getData() {
        return data;
    }

    public void setData(List<Ingredients> data) {
        this.data = data;
    }
}
