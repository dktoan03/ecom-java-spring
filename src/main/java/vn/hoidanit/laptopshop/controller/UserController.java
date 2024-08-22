package vn.hoidanit.laptopshop.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.repository.UserRepository;
import vn.hoidanit.laptopshop.sevice.UserService;

@Controller
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @RequestMapping("/")
  public String getHomePage(Model model) {
    List<User> users = this.userService.getAllUsers();
    // System.out.println(users);
    model.addAttribute("eric", users);
    return "hello";
  }

  @RequestMapping("/admin/user")
  public String getUserPage(Model model) {
    List<User> users = this.userService.getAllUsers();
    model.addAttribute("allUsers", users);

    return "admin/user/table-user";
  }

  @RequestMapping("/admin/user/{id}")
  public String getUserDetailPage(Model model, @PathVariable long id) {

    model.addAttribute("id", id);
    User user = this.userService.getUserById(id);
    model.addAttribute("user", user);

    return "admin/user/user-detail";
  }

  @RequestMapping("/admin/user/create")
  public String getCreateUserPage(Model model) {
    model.addAttribute("newUser", new User());
    return "admin/user/create";
  }

  @RequestMapping(value = "/admin/user/create1", method = RequestMethod.POST)
  public String createUserPage(Model model, @ModelAttribute("newUser") User newUser) {
    this.userService.handleSaveUser(newUser);
    // model.addAttribute("eric", newUser);
    return "redirect:/admin/user";

    // return "hello";

  }

  @RequestMapping("/admin/user/update/{id}")
  public String getUpdateUserPage(Model model, @PathVariable long id) {
    User user = this.userService.getUserById(id);
    model.addAttribute("newUser", user);
    return "admin/user/update";
  }

  @PostMapping(value = "/admin/user/update1")
  public String updateUserPage(Model model, @ModelAttribute("newUser") User updatedUser) {
    User user = this.userService.getUserById(updatedUser.getId());
    if (user != null) {
      user.setAddress(updatedUser.getAddress());
      user.setFullName(updatedUser.getFullName());
      user.setPhone(updatedUser.getPhone());
      this.userService.handleSaveUser(user);

    }
    return "redirect:/admin/user";
  }

  @GetMapping("/admin/user/delete/{id}")
  public String getDeleteUserPage(Model model, @PathVariable long id, @ModelAttribute("newUser") User updatedUser) {
    model.addAttribute("id", id);
    model.addAttribute("newUser", new User());

    User user = this.userService.getUserById(id);

    return "admin/user/delete";
  }

  @PostMapping(value = "/admin/user/deletee")
  public String deleteUserPage(Model model, @ModelAttribute("newUser") User user) {
    this.userService.deleteUserById(user.getId());

    return "redirect:/admin/user";
  }
}
