import javax.swing.*;
import javax.swing.table.DefaultTableModel; //библиотеки для граф. интерфейса
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class TodoFrame extends JFrame {
  private TaskManager manager;
  private JTable table;
  private DefaultTableModel model;
  private List<Task> currentDisplayedTasks;

  public TodoFrame(TaskManager manager) {
    this.manager = manager;
    this.currentDisplayedTasks = manager.getAll();
    initializeUI();
  }

//инициализация пользовательского интерфейса и настраивание окна 

  private void initializeUI() {
    setTitle("To-Do List");
    setSize(800, 500);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

    JPanel topPanel = createTopPanel();
    initializeTable();

    add(topPanel, BorderLayout.NORTH);
    add(new JScrollPane(table), BorderLayout.CENTER);

    setupStyling(topPanel);
    setVisible(true);
  }


  private JPanel createTopPanel() {
    JPanel topPanel = new JPanel();
    JButton addBtn = new JButton("Добавить");
    JButton editBtn = new JButton("Редактировать");
    JButton delBtn = new JButton("Удалить");
    JButton sortBtn = new JButton("Сортировать");
    JButton searchBtn = new JButton("Поиск");
    JTextField searchField = new JTextField(15);

//добавление компонентов на панель(кнопки, поля для ввода)

    topPanel.add(addBtn);
    topPanel.add(editBtn);
    topPanel.add(delBtn);
    topPanel.add(sortBtn);
    topPanel.add(searchField);
    topPanel.add(searchBtn);

    addBtn.addActionListener(e -> openAddDialog());  // при нажатии на кнопку вызывается соответствующий метод  
    editBtn.addActionListener(e -> openEditDialog());
    delBtn.addActionListener(e -> deleteSelectedTask());
    sortBtn.addActionListener(e -> sortTasks());
    searchBtn.addActionListener(e -> searchTasks(searchField.getText()));

    return topPanel;
  }

  private void initializeTable() {
    String[] cols = { "Название", "Описание", "Дата", "Приоритет" };

   //создаем модели таблицы такие, чтобы в ней нельзя было редактировать ячейки 

    model = new DefaultTableModel(cols, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };
    table = new JTable(model);
    refreshTable(manager.getAll());  // добавляются строки с задачами 
  }

  //добавляем новую задачу 
  private void openAddDialog() {
    try {
      String title = JOptionPane.showInputDialog("Название задачи:");
      if (title == null || title.trim().isEmpty())
        return;

      String desc = JOptionPane.showInputDialog("Описание:");
      String dateStr = JOptionPane.showInputDialog("Срок (в формате YYYY-MM-DD):");
      String prioStr = JOptionPane.showInputDialog("Приоритет (1 - высокий, 2 - средний, 3 - низкий):");

      if (dateStr != null && prioStr != null) {
        LocalDate date = LocalDate.parse(dateStr);
        int priority = Integer.parseInt(prioStr);

        if (priority < 1 || priority > 3) {
          showError("Приоритет должен быть от 1 до 3");
          return;
        }

        Task task = new Task(title.trim(), desc != null ? desc : "", date, priority);
        manager.add(task);
        refreshTable(manager.getAll());
      }
    } catch (DateTimeParseException e) {
      showError("Неверный формат даты! Используйте YYYY-MM-DD");
    } catch (NumberFormatException e) {
      showError("Приоритет должен быть числом от 1 до 3");
    } catch (Exception e) {
      showError("Ошибка: " + e.getMessage());
    }
  }

  private void openEditDialog() {   //проверяется выделена ли задача
    int row = table.getSelectedRow();
    if (row < 0) {
      showError("Выберите задачу для редактирования!");
      return;
    }

    try {
      Task task = currentDisplayedTasks.get(row);

      String newTitle = JOptionPane.showInputDialog("Новое название:", task.getTitle());
      if (newTitle == null || newTitle.trim().isEmpty())
        return;

      String newDesc = JOptionPane.showInputDialog("Новое описание:", task.getDescription());
      String newDateStr = JOptionPane.showInputDialog("Новая дата (YYYY-MM-DD):", task.getDueDate());
      String newPrioStr = JOptionPane.showInputDialog("Новый приоритет (1-3):", String.valueOf(task.getPriority()));

      if (newDateStr != null && newPrioStr != null) {
        task.setTitle(newTitle.trim());
        task.setDescription(newDesc != null ? newDesc : "");
        task.setDueDate(LocalDate.parse(newDateStr));

        int newPriority = Integer.parseInt(newPrioStr);
        if (newPriority < 1 || newPriority > 3) {
          showError("Приоритет должен быть от 1 до 3");
          return;
        }
        task.setPriority(newPriority);

        refreshTable(currentDisplayedTasks);
      }
    } catch (DateTimeParseException e) {
      showError("Неверный формат даты! Используйте YYYY-MM-DD");
    } catch (NumberFormatException e) {
      showError("Приоритет должен быть числом от 1 до 3");
    } catch (Exception e) {
      showError("Ошибка: " + e.getMessage());
    }
  }

  private void deleteSelectedTask() {
    int row = table.getSelectedRow();
    if (row < 0) {
      showError("Выберите задачу для удаления!");
      return;
    }

    Task taskToDelete = currentDisplayedTasks.get(row);
    int confirm = JOptionPane.showConfirmDialog(this,
        "Удалить задачу '" + taskToDelete.getTitle() + "'?",
        "Подтверждение удаления", JOptionPane.YES_NO_OPTION);

    if (confirm == JOptionPane.YES_OPTION) {
      manager.remove(taskToDelete);
      refreshTable(manager.getAll());
    }
  }

  private void sortTasks() {
    manager.sortByDate();
    refreshTable(manager.getAll());
  }

  private void searchTasks(String keyword) {
    if (keyword == null || keyword.trim().isEmpty()) {
      refreshTable(manager.getAll());
    } else {
      refreshTable(manager.search(keyword.trim()));
    }
  }

  private void refreshTable(List<Task> tasks) {
    currentDisplayedTasks = tasks;
    model.setRowCount(0);
    for (Task t : tasks) {
      model.addRow(new Object[] {
          t.getTitle(),
          t.getDescription(),
          t.getDueDate(),
          getPriorityText(t.getPriority())
      });
    }
  }

  private String getPriorityText(int priority) {
    switch (priority) {
      case 1:
        return "Высокий";
      case 2:
        return "Средний";
      case 3:
        return "Низкий";
      default:
        return "Неизвестный";
    }
  }

  private void showError(String message) {
    JOptionPane.showMessageDialog(this, message, "Ошибка", JOptionPane.ERROR_MESSAGE);
  }

  private void setupStyling(JPanel topPanel) {
    Color bgColor = new Color(245, 248, 250);
    Color panelColor = new Color(230, 236, 245);
    Color tableColor = new Color(252, 252, 255);
    Color accentColor = new Color(255, 105, 180); 
    Color hoverColor = new Color(255, 20, 147);
    Color textColor = Color.WHITE;

    getContentPane().setBackground(bgColor);
    topPanel.setBackground(panelColor);
    table.setBackground(tableColor);
    table.setGridColor(new Color(220, 220, 230));
    table.setSelectionBackground(new Color(255, 182, 193)); 

    for (Component comp : topPanel.getComponents()) {
      if (comp instanceof JButton) {
        JButton btn = (JButton) comp;

        // основные настройки кнопки
        btn.setBackground(accentColor);
        btn.setForeground(textColor);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));


        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {  // метод, с помощью которого при наведении курсора цвет кнопки меняется, а при уходе возвращается обратно
          public void mouseEntered(java.awt.event.MouseEvent evt) {
            btn.setBackground(hoverColor);
          }

          public void mouseExited(java.awt.event.MouseEvent evt) {
            btn.setBackground(accentColor);
          }

          public void mousePressed(java.awt.event.MouseEvent evt) {
            btn.setBackground(hoverColor.darker());
          }

          public void mouseReleased(java.awt.event.MouseEvent evt) {
            btn.setBackground(hoverColor);
          }
        });
      }
    }

    // обновляем внешний вид
    SwingUtilities.updateComponentTreeUI(this);
  }
}
