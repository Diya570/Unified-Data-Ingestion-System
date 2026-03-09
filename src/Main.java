public class Main {
    public static void main(String[] args) {
        DBInitializer.init();
        javax.swing.SwingUtilities.invokeLater(()->{
            new SwingUI().setVisible(true);
        });
        
    }
}
