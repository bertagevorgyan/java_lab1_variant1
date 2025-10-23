import javax.swing.*;  // библиотека swing для создания графического интерфейса

public class TodoApp {
  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
      e.printStackTrace();   //для интерфейса программы устанавливается тот же стиль, что и у ОС 
    }

    SwingUtilities.invokeLater(() -> new TodoFrame(new TaskManager())); //создается стартовое окно и ему передаётся объект TaskManager
  }
}
