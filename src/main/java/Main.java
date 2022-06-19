import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static final String REMOTE_SERVICE_URI = "https://jsonplaceholder.typicode.com/posts";
    public static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        // Создание и настройка Java клиента
        //добавим библиотеку apache в зависимости

        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setUserAgent("Marsel")
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000) // максимальное время ожидания подключения к серверу
                        .setSocketTimeout(30000) // максимальное время ожидания получения данных
                        .setRedirectsEnabled(false) // возможность следовать редиректу в ответе (перенаправление)
                        .build())
                .build();

        //Вызов удаленного сервера

        HttpGet request = new HttpGet(REMOTE_SERVICE_URI);
        request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());

        //Отправка запроса
        CloseableHttpResponse response = httpClient.execute(request);

        // Выаод полученных заголовков
        Arrays.stream(response.getAllHeaders()).forEach(System.out::println);
        System.out.println();

//         Чтение тела ответа
//        String body = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
//        System.out.println(body);

        // Сервер вернул нам список постов пользователей  теукстом в формате json
        // для преобразования json в java добавим библиотеку Jackson в зависимости
        // создадим класс в Java Post для преобразования
        // пропишем в конструкторе Post аннотации @JsonProperty (чтобы конструктор понимал,какие поля в json соответствуют полям в java классе
        //создадим в Main объект библиотеки Jackson типа ObjectMapper(нужен для десериализации json в Java.Post). public static

        //преобразуем json в java
        List<Post> posts = mapper.readValue(response.getEntity().getContent(), new TypeReference<List<Post>>() {
        });
        posts.forEach(System.out::println);
        //закомментил вывод на консоль body, так как пробрасывается исключение. Причину не понимаю.


    }
}
