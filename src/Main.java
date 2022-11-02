public class Main {
    public static void main(String[] args) {
        Connect work = Connect.getInstance();
        System.out.println(work.getAllStudents());
        System.out.println(work.getAllCities());
        work.addStudent("Irina", "Mazur", "Minsk");
        System.out.println(work.getAllStudents());
        System.out.println(work.getAllCities());
        work.removeStudent("Irina", "Mazur", "Minsk");
        work.removeCity("Minsk");
        System.out.println(work.getAllStudents());
        System.out.println(work.getAllCities());
    }
}
