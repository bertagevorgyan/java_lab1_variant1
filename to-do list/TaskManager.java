import java.util.*;
import java.time.LocalDate;

public class TaskManager {
  private List<Task> tasks = new ArrayList<>();

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
    return new ArrayList<>(tasks);
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
      }
    }
    return result;
  }

  public int size() {
    return tasks.size();
  }

  public boolean isEmpty() {
    return tasks.isEmpty();
  }

  public void clear() {
    tasks.clear();
  }

  public Task get(int index) {
    if (index >= 0 && index < tasks.size()) {
      return tasks.get(index);
    }
    return null;
  }
}
