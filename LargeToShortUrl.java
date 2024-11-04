import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;

public class LargeToShortUrl {
    private Map<String, String> shortToLongUrlMap;
    private Map<String, String> longToShortUrlMap;
    private static final String DATA_FILE = "link_data.txt";

    public LargeToShortUrl() {
        shortToLongUrlMap = new HashMap<>();
        longToShortUrlMap = new HashMap<>();
        loadData();
    }

    public String shortenUrl(String longUrl) {
        if (longToShortUrlMap.containsKey(longUrl)) {
            return longToShortUrlMap.get(longUrl);
        }
        String shortUrl = generateShortUrl(longUrl);
        while (shortToLongUrlMap.containsKey(shortUrl)) {
            shortUrl = generateShortUrl(longUrl);
        }
        shortToLongUrlMap.put(shortUrl, longUrl);
        longToShortUrlMap.put(longUrl, shortUrl);
        saveData();
        return shortUrl;
    }

    public String expandUrl(String shortUrl) {
        return shortToLongUrlMap.get(shortUrl);
    }

    private String generateShortUrl(String longUrl)
    {
        return String.valueOf(longUrl.hashCode());
    }

    private void saveData()
    {
        try (FileWriter writer = new FileWriter(DATA_FILE))
        {
            for (Map.Entry<String, String> entry : longToShortUrlMap.entrySet())
            {
                writer.write(entry.getKey() + "," + entry.getValue() + "\n");
            }
        } catch (IOException e)
        {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    private void loadData()
    {
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE)))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                String[] parts = line.split(",");
                longToShortUrlMap.put(parts[0], parts[1]);
                shortToLongUrlMap.put(parts[1], parts[0]);
            }
        } catch (IOException e) {
            // Ignore if file doesn't exist or can't be read
        }
    }

    public static void main(String[] args)
    {
        LargeToShortUrl linkShortener = new LargeToShortUrl();
        Scanner scanner = new Scanner(System.in);
        while (true)
        {
            System.out.println("1. Shorten URL");
            System.out.println("2. Expand URL");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine(); // Consume newline left-over
            switch (option)
            {
                case 1:
                    System.out.print("Enter long URL: ");
                    String longUrl = scanner.nextLine();
                    String shortUrl = linkShortener.shortenUrl(longUrl);
                    System.out.println("Shortened URL: " + shortUrl);
                    break;
                case 2:
                    System.out.print("Enter short URL: ");
                    String shortUrl1 = scanner.nextLine();
                    String longUrl1 = linkShortener.expandUrl(shortUrl1);
                    if (longUrl1 == null) {
                        System.out.println("Invalid short URL");
                    } else {
                        System.out.println("Expanded URL: " + longUrl1);
                    }
                    break;
                case 3:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option");
            }
        }
    }
}