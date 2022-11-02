public class Main {
    public static void main(String[] args) throws InterruptedException {
        Connect work = Connect.getInstance();
        System.out.println(work.getAllStudents());
        System.out.println(work.getAllCities());
        work.addStudent("Irina", "Mazur", "Minsk");
        Thread.sleep(1000);
        System.out.println(work.getAllStudents());
        System.out.println(work.getAllCities());
        Thread.sleep(1000);
        work.removeStudent("Irina", "Mazur", "Minsk");
        work.removeCity("Minsk");
        Thread.sleep(1000);
        System.out.println(work.getAllStudents());
        System.out.println(work.getAllCities());
    }
}
