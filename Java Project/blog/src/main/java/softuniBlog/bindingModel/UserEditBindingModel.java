package softuniBlog.bindingModel;

import java.util.ArrayList;
import java.util.List;

public class UserEditBindingModel extends UserBindingModel {

    private List<Integer> roles;

    private Integer positionId;

    public UserEditBindingModel() {
        this.roles = new ArrayList<>();
    }

    public List<Integer> getRoles() {
        return roles;
    }

    public void setRoles(List<Integer> roles) {
        this.roles = roles;
    }

    public Integer getPositionId() {
        return positionId;
    }

    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
    }
}
