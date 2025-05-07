package invest_wise;

import javax.swing.*;

public class Home extends login_signup{
    public Home(){
        styles styleHelper = new styles();
        styleHelper.window();
        JButton reset = styledButton("Update Password");
    }
}
