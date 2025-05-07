package invest_wise;

import javax.swing.*;

public class Home extends login_signup{
    public Home(){
        styles styleHelper = new styles();
        styleHelper.window();
        JButton financial = styledButton("Financial goals");
        financial .addActionListener(e -> {
            new Home();
            dispose();
        });
        add(financial);
    }
}
