package com.example.demo.RestControllers;

import com.example.demo.Entity.Party;
import com.example.demo.Servises.PartyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
/**
 * Контроллер для работы с мероприятиями (parties).
 * Обрабатывает запросы для получения, создания, обновления, удаления мероприятий,
 * а также загрузки изображений.
 */
@RestController
@RequestMapping("/api/parties")
public class PartyRestController {

    @Autowired
    private PartyUserService partyUserService;
    /**
     * Получение списка мероприятий с возможностью сортировки по имени или дате.
     * Поддерживает сортировку по возрастанию или убыванию.
     *
     * @param sort  Параметр сортировки (по имени или дате). Может быть {@code "name"} или {@code "date"}.
     * @param order Направление сортировки (по возрастанию или убыванию). Может быть {@code "asc"} или {@code "desc"}.
     * @return Список мероприятий, отсортированный по указанным критериям.
     */

    @GetMapping
    public List<Party> getParties(@RequestParam(required = false) String sort,
                                  @RequestParam(required = false) String order) {
        List<Party> listParty;

        if ("name".equals(sort)) {
            listParty = "desc".equals(order) ? partyUserService.findAllSortedByNameDesc() : partyUserService.findAllSortedByNameAsc();
        } else if ("date".equals(sort)) {
            listParty = "desc".equals(order) ? partyUserService.findAllSortedByDateDesc() : partyUserService.findAllSortedByDateAsc();
        } else {
            listParty = partyUserService.listAll(""); // Если сортировка не указана, возвращаем все мероприятия
        }
        return listParty;
    }

    /**
     * Получение информации о мероприятии по ID.
     *
     * @param id Идентификатор мероприятия.
     * @return Мероприятие с указанным ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Party> getPartyById(@PathVariable("id") Integer id) {
         Party party = partyUserService.findById(id);
        return ResponseEntity.ok(party);
    }
    /**
     * Загрузка изображения для мероприятия.
     * Загружает изображение и сохраняет его в локальной директории.
     *
     * @param image Файл изображения, который нужно загрузить.
     * @return Ответ с результатом загрузки.
     */

    @PostMapping("/uploadImage")
    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile image) {
        try {
            Path path = Paths.get("src/main/resources/static/images" + image.getOriginalFilename());
            image.transferTo(path);
            return ResponseEntity.ok("File uploaded successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file");
        }
    }

    /**
     * Создание нового мероприятия.
     *
     * @param name        Название мероприятия.
     * @param date        Дата проведения мероприятия в формате "yyyy-MM-dd".
     * @param description Описание мероприятия.
     * @param ageLimit    Возрастное ограничение для мероприятия.
     * @param image       Изображение для мероприятия.
     * @return Ответ с созданным мероприятием.
     */
    @PostMapping
    public ResponseEntity<Party> createParty(@RequestParam String name,
                                             @RequestParam String date,
                                             @RequestParam String description,
                                             @RequestParam Integer ageLimit,
                                             @RequestParam MultipartFile image) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date eventDate = null;
        try {
            eventDate = sdf.parse(date);
        } catch (ParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Party party = new Party();
        party.setName(name);
        party.setDate(eventDate);
        party.setDescription(description);
        party.setAgeLimit(ageLimit);

        String fileName = image.getOriginalFilename();
        partyUserService.saveParty(party);
        return ResponseEntity.status(HttpStatus.CREATED).body(party);
    }

    /**
     * Обновление существующего мероприятия.
     *
     * @param id    Идентификатор мероприятия.
     * @param party Обновленные данные мероприятия.
     * @return Ответ с обновленным мероприятием.
     */

    @PutMapping("/{id}")
    public ResponseEntity<Party> updateParty(@PathVariable("id") Integer id, @RequestBody Party party) {
        if (partyUserService.findById(id)==null) {
            return ResponseEntity.notFound().build();
        }
        party.setId(id);
        partyUserService.saveParty(party);
        return ResponseEntity.ok(party);
    }

    /**
     * Удаление мероприятия по ID.
     *
     * @param id Идентификатор мероприятия, которое необходимо удалить.
     * @return Ответ без содержимого, подтверждающий успешное удаление.
     */

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParty(@PathVariable("id") Integer id) {
        if (partyUserService.findById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        partyUserService.deleteByIdParty(id);
        return ResponseEntity.noContent().build();
    }
}
