package softuniBlog.bindingModel;


import javax.validation.constraints.NotNull;
import java.util.List;

public class SearchBindingModel {

    @NotNull
    private String searchText;

    private List<String> searchTypes;

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public List<String> getSearchTypes() {
        return searchTypes;
    }

    public void setSearchTypes(List<String> searchTypes) {
        this.searchTypes = searchTypes;
    }
}
