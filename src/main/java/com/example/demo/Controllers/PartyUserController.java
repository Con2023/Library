package com.example.demo.Controllers;

import com.example.demo.Repos.PartyRepo;
import com.example.demo.Repos.PartyUserRepo;
import com.example.demo.Servises.PartyUserService;
import com.example.demo.Entity.Party;
import com.example.demo.Entity.PartyUser;
import com.example.demo.Repos.RepoUser;
import com.example.demo.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

/**
 * Контроллер для управления вечеринками и участниками вечеринок.
 * Обрабатывает запросы на создание, редактирование, удаление вечеринок, а также регистрацию пользователей на вечеринки.
 */

@Controller
public class PartyUserController {
    @Autowired
    private PartyUserService partyUserService;
    @Autowired
    private PartyUserRepo partyUserRepo;
    @Autowired
    private RepoUser repoUser;
    @Autowired
    private PartyRepo partyRepo;

    @Value("${upload.dir}")
    private String uploadDir;

   /* *//**
     * Отображение страницы для управления мероприятиями.
     * Позволяет сортировать и фильтровать мероприятия по ключевому слову.
     *
     //* @param model   Модель для передачи атрибутов на страницу.
     * //@param keyword Ключевое слово для фильтрации.
     //* @param sort    Параметр сортировки (по имени или дате).
   //  * @param order   Направление сортировки (по возрастанию или убыванию).
     * @return Страница для управления вечеринками.
     */
    @GetMapping("/manageParty")
    public String managePartyPage() {
        return "manageParty";
    }

   @GetMapping("/addParty")
   public String addPartyPage() {
       return "addParty";
   }

    /**
     * Сохранение новой мероприятия.
     * Загружает изображение для мероприятия и сохраняет данные о мероприятии в базе данных.
     *
     * @param party Объект мероприятие, который будет сохранен.
     * @param image Изображение для мероприятия.
     * @return Перенаправление на страницу управления мероприятиями.
     */
    @RequestMapping(value = "/saveP")
    public String saveParty(@ModelAttribute("party") Party party, @RequestParam("image") MultipartFile image) {
        if (!image.isEmpty()) {
            try {
                String filename = StringUtils.cleanPath(image.getOriginalFilename());
                Path path = Paths.get(uploadDir, filename);
                Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                party.setImageUrl("/images/" + filename);
                party.setImageUrl(filename);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        partyUserService.saveParty(party);
        return "redirect:/manageParty";
    }

    /**
     * Получение изображения по имени файла.
     * Позволяет отображать изображение мероприятия на странице.
     *
     * @param filename Имя файла изображения.
     * @return Массив байтов изображения.
     * @throws IOException Если произошла ошибка при чтении файла.
     */

    @GetMapping("/image/{filename}")
    @ResponseBody
    public byte[] getImage(@PathVariable String filename) throws IOException {
        Path path = Paths.get("src/main/resources/static/images/image/" + filename);
        return Files.readAllBytes(path);
    }

    /**
     * Обработка исключения, возникающего при попытке загрузить слишком большой файл.
     *
     * @param exc                Исключение, связанное с превышением максимального размера файла.
     * @param redirectAttributes Модель для передачи сообщения об ошибке.
     * @return Перенаправление на страницу добавления вечеринки с сообщением об ошибке.
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxUploadSizeExceeded(MaxUploadSizeExceededException exc, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", "Файл слишком большой. Попробуйте загрузить файл меньшего размера.");
        return "redirect:/addParty";
    }

    /**
     * Отображение страницы с мероприятиями, на которые пользователь может зарегистрироваться.
     *
     * @param model   Модель для передачи списка мероприятий.
     * @param keyword Ключевое слово для фильтрации мероприятий.
     * @return Страница для просмотра мероприятий.
     */
    @RequestMapping("/watch_party")
    public String watchParty(Model model, @Param("keyword") String keyword) {
        List<Party> listParty = partyUserService.listAll(keyword);
        model.addAttribute("listParty", listParty);
        model.addAttribute("keyword", keyword);
        return "/watch_party";
    }

    /**
     * Регистрация пользователя на вечеринку.
     * Пользователь регистрируется на указанную вечеринку.
     *
     * @param partyUser Объект регистрации пользователя на вечеринку.
     * @param partyId   Идентификатор вечеринки.
     * @param model     Модель для передачи возможных ошибок при регистрации.
     * @return Перенаправление на страницу профиля пользователя или сообщение об ошибке.
     */
    @RequestMapping("/registerParty")
    public String registerParty(@ModelAttribute("partyUser") PartyUser partyUser, @RequestParam int partyId, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = repoUser.findUserByLogin(authentication.getName());
        Party party = partyRepo.findById(partyId);
        try {
            partyUserService.registerParty(partyUser, user, party);
            return "redirect:/userPage";
        } catch (IllegalStateException e) {
            model.addAttribute("registrationError", e.getMessage());
            return "redirect:/my_watch_party";
        }
    }

    /**
     * Отписка пользователя от мероприятия.
     * Удаляет запись о регистрации пользователя на мероприятие.
     *
     * @param id Идентификатор записи о регистрации пользователя.
     * @return Перенаправление на страницу с перечнем мероприятий пользователя.
     */
    @RequestMapping("/unsubscribeParty/{id}")
    public String unsubscribeParty(@PathVariable Integer id) {
        partyUserService.deleteByIdPartyUser(id);
        return "redirect:/my_watch_party";
    }

    /**
     * Отображение страницы с мероприятиями, на которые пользователь уже зарегистрирован.
     *
     * @param model Модель для передачи списка мероприятий пользователя.
     * @return Страница с перечнем зарегистрированных мероприятий пользователя.
     */
    @RequestMapping("/my_watch_party")
    public String myParty(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = repoUser.findUserByLogin(authentication.getName());
        List<PartyUser> listMyParty = partyUserRepo.findAllByUser_ID(user.getID());
        model.addAttribute("listMyParty", listMyParty);
        return "/my_watch_party";
    }

    /**
     * Удаление мероприятия из системы.
     *
     * @param id Идентификатор мероприятия.
     * @return Перенаправление на страницу управления мероприятиями.
     */
    @RequestMapping("/delete_party/{id}")
    public String deleteParty(@PathVariable Integer id) {
        partyUserService.deleteByIdParty(id);
        return "redirect:/manageParty";
    }

    /**
     * Отображение страницы для редактирования информации о мероприятии.
     *
     * @param id    Идентификатор мероприятия, которую нужно отредактировать.
     * @param model Модель для передачи объекта мероприятие в форму редактирования.
     * @return Страница редактирования мероприятия.
     */
    @RequestMapping(value = "/edit_party/{id}")
    public String updateParty(@PathVariable Integer id, Model model) {
        Party party = partyUserService.findById(id);
        model.addAttribute("party", party);
        return "edit_party";
    }

}

