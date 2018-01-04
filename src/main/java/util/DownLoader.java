package util;

import constant.CacheConsts;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.UncheckedIOException;
import org.jsoup.nodes.Document;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;



public class DownLoader {

    /**
     * Use https://github.com/jhao104/proxy_pool
     *      as proxy pool (url with "127.0.0.1:5010/get/")
     *  to Download document from specific url
     */
    public static Document getDocByUrl(String url, int useType) {
        // add hostname into url
        if (! url.startsWith(CacheConsts.HOST)) {
            url = CacheConsts.HOST + url;
        }

        String proxyUrl = null;
        // String array: index 0 as host, index 1 as port
        String[] urlAndPort;
        // doc for return
        Document doc = null;
        // try times
        int time = 10;

        while (time -- > 0) {
            System.out.println(new String(new char[80]).replace("\0", "-"));

            try {
                System.out.println("remain " + time + " time to try download ...");
                // get proxy url
                proxyUrl = Jsoup.connect(CacheConsts.GET_PROXY_URL).get().text();
                urlAndPort = proxyUrl.trim().split(":");
                if (useType == CacheConsts.useJsoup) {
                    doc = downloadByJsoup(url, urlAndPort);
                } else if(useType == CacheConsts.useSelenium) {
                    doc = downloadBySelenium(url, urlAndPort);
                }

                if (doc == null) {
                    System.out.println("doc null");
                    continue;
                }
            } catch (UncheckedIOException e) {
                System.out.println("catch org.jsoup.UncheckedIOException...request again...");
                continue;
            } catch (TimeoutException e) {
                System.out.println("catch a TimeoutException...request again...");
                continue;
            } catch (WebDriverException e) {
                e.printStackTrace();
                System.out.println("catch a WebDriverException...request again...");
                continue;
            } catch (SocketTimeoutException e) {
                if (proxyUrl == null || "".equals(proxyUrl.trim())) {
                    System.out.println("get proxy url fail");
                    time ++;
                }
                System.out.println("catch a SocketTimeoutException...request again...");
                continue;
            } catch (ConnectException e) {
                System.out.println("catch a ConnectException...request again...");
                continue;
            } catch (HttpStatusException e) {
                System.out.println("catch a HttpStatusException...request again...");
                continue;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("unknown Exception...request again...");
                continue;
            }
            System.out.println("download success!");
            break;
        }

        return doc;
    }

    private static Document downloadByJsoup(String url, String[] urlAndPort) throws IOException {
        System.out.println("Jsoup connect <" + url + "> via <" + urlAndPort[0] + ":" + urlAndPort[1] + ">");
        Document doc;

        doc = Jsoup.connect(url)
                .proxy(urlAndPort[0], Integer.parseInt(urlAndPort[1]))
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                .header("Accept-Language", "zh-CN,zh;q=0.9")
                .header("Host", "music.163.com")
                .header("Referer", "http://music.163.com/")
                .header("Upgrade-Insecure-Requests", "1")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36")
                .get();
        return doc;
    }

    private static Document downloadBySelenium(String url, String[] urlAndPort) {
        System.out.println("Selenium connect <" + url + "> via <" + urlAndPort[0] + ":" + urlAndPort[1] + ">");
        Document doc;

        Proxy proxy = new org.openqa.selenium.Proxy();
        proxy.setHttpProxy(String.join(":", urlAndPort));

        // Optional, if not specified, WebDriver will search your path for chromedriver.
        System.setProperty("webdriver.chrome.driver", "./chromedriver.exe");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setHeadless(true);
        chromeOptions.setProxy(proxy);
        chromeOptions.setBinary("C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe");

        WebDriver driver = new ChromeDriver(chromeOptions);
        driver.get(url);
        WebElement webElement = driver.findElement(By.id("g_iframe"));
        driver.switchTo().frame(webElement);
        doc = Jsoup.parse(driver.getPageSource(), url);

        driver.quit();
        return doc;
    }

}
