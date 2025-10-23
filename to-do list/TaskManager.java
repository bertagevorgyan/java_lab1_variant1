import java.util.*;
import java.time.LocalDate;    // для работы с датами 

public class TaskManager {
  private List<Task> tasks = new ArrayList<>();  // список для хранения всех задач

  public void add(Task task) {
    tasks.add(task);
  }

  public void remove(int index) {
    if (index >= 0 && index < tasks.size()) {
      tasks.remove(index);
    }
  }

  public void remove(Task task) {
    tasks.remove(task);
  }

  public List<Task> getAll() {
    return new ArrayList<>(tasks);  //возвращается копия списка задач
  }

  public void sortByDate() {
    tasks.sort(Comparator.comparing(Task::getDueDate));
  }

  public List<Task> search(String keyword) {
    if (keyword == null || keyword.trim().isEmpty()) {
      return getAll();
    }

    String searchTerm = keyword.toLowerCase().trim();
    List<Task> result = new ArrayList<>();

    for (Task t : tasks) {
      if ((t.getTitle() != null && t.getTitle().toLowerCase().contains(searchTerm)) ||
          (t.getDescription() != null && t.getDescription().toLowerCase().contains(searchTerm))) {
        result.add(t);
      }   // так мы осуществляем поиск без учетка регистра и поиск по ключевому слову 
    }
    return result;
    
  }
}
