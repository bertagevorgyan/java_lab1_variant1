import java.time.LocalDate;
import java.util.Objects;

public class Task {
  private String title;
  private String description;
  private LocalDate dueDate;
  private int priority; // 1 - высокий, 2 - средний, 3 - низкий

  public Task(String title, String description, LocalDate dueDate, int priority) {
    if (title == null || title.trim().isEmpty()) {
      throw new IllegalArgumentException("Название задачи не может быть пустым");
    }
    this.title = title;
    this.description = description != null ? description : "";
    this.dueDate = dueDate;
    setPriority(priority);
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    if (title == null || title.trim().isEmpty()) {
      throw new IllegalArgumentException("Название задачи не может быть пустым");
    }
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description != null ? description : "";
  }

  public LocalDate getDueDate() {
    return dueDate;
  }

  public void setDueDate(LocalDate dueDate) {
    this.dueDate = dueDate;
  }

  public int getPriority() {
    return priority;
  }

  public void setPriority(int priority) {
    if (priority < 1 || priority > 3) {
      throw new IllegalArgumentException("Приоритет должен быть от 1 до 3");
    }
    this.priority = priority;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    Task task = (Task) o;
    return priority == task.priority &&
        Objects.equals(title, task.title) &&
        Objects.equals(description, task.description) &&
        Objects.equals(dueDate, task.dueDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title, description, dueDate, priority);
  }

  @Override
  public String toString() {
    String priorityText;
    switch (priority) {
      case 1:
        priorityText = "высокий";
        break;
      case 2:
        priorityText = "средний";
        break;
      case 3:
        priorityText = "низкий";
        break;
      default:
        priorityText = "неизвестный";
        break;
    }
    return title + " (до " + dueDate + ", приоритет: " + priorityText + ")";
  }
}
