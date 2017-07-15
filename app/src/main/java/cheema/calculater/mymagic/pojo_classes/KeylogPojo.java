package cheema.calculater.mymagic.pojo_classes;

/**
 * Created by srb on 7/7/17.
 */

public class KeylogPojo {

    public KeylogPojo(String key, String ans){
        this.key = key;
        this.ans = ans;
    }

    public String getKey() {
        return key;
    }

    public String getAns() {
        return ans;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }

    //variables
    private String key;
    private String ans;
}
