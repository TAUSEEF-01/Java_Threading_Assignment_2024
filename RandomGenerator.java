import java.util.*;

class RandomGenerator {
    private String allChars = "abcdefghijklmnopqrstuvwxyz";
    private Random rndm = new Random();

    public String generateName() {
        int n = rndm.nextInt(15);
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {
            int randomIndex = rndm.nextInt(allChars.length());
            sb.append(allChars.charAt(randomIndex));
        }

        return sb.toString();
    }

    public String generateAccountNumber() {
        StringBuilder sb = new StringBuilder(12);

        for (int i = 0; i < 2; i++) {
            int randomIndex = rndm.nextInt(allChars.length());
            sb.append(allChars.charAt(randomIndex));
        }

        for (int i = 0; i < 10; i++) {
            int randomDigit = rndm.nextInt(10);
            sb.append(randomDigit);
        }

        return sb.toString();
    }

    public int generateNumber(int limit) {
        int value = rndm.nextInt(limit);

        return value;
    }

}
