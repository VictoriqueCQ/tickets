package cn.tickets.controller;

import cn.tickets.entity.AccountEntity;
import cn.tickets.entity.MemberEntity;
import cn.tickets.entity.VenueEntity;
import cn.tickets.repository.AccountRepository;
import cn.tickets.repository.MemberRepository;
import cn.tickets.repository.VenueRepository;
import cn.tickets.util.Default;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.thymeleaf.TemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class AccountController {
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("")
    private String sender;


    private final AccountRepository accountRepository;
    private final MemberRepository memberRepository;
    private final VenueRepository venueRepository;

    @Autowired
    public AccountController(AccountRepository accountRepository, MemberRepository memberRepository, VenueRepository venueRepository) {
        this.accountRepository = accountRepository;
        this.memberRepository = memberRepository;
        this.venueRepository = venueRepository;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(HttpSession session, @RequestParam("username") String mail, @RequestParam("password") String password) {
        AccountEntity result;
        if (isEmail(mail)) {
            result = accountRepository.findByEmailAndPassword(mail, password);
        } else {
            VenueEntity venueEntity = venueRepository.findBySystemid(mail);
            if (venueEntity != null) {
                result = accountRepository.findByIdAndPassword(venueEntity.getId(), password);
            } else {
                return "account/index";
            }

        }


        if (result != null) {
            System.err.println(result);
            session.setAttribute(Default.USER_ID, result.getId());
            switch (result.getType()) {
                case 0:
                    MemberEntity memberEntity = memberRepository.findOne(result.getId());
                    System.err.println("state:" + memberEntity.getState());
                    if (memberEntity.getState() == 1 && memberEntity.getQualification() == 1) {
                        return "member/index";
                    } else {
                        return "member/attention";
                    }
                case 1:
                    VenueEntity venueEntity = venueRepository.findOne(result.getId());
                    if (venueEntity.getApprove() == 2||venueEntity.getApprove()==1) {
                        return "venue/index";
                    } else {
                        return "account/index";
                    }

                case 2:
                    return "manager/index";
                default:
                    return "account/index";
            }
        }
        return "account/index";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(Model model, String mail, String password, String type) {

        AccountEntity entity = accountRepository.findByEmail(mail);

        if (entity != null) {
            model.addAttribute("error", "Mail Existed");
            return "account/register";
        } else {
            int accountType = 0;
            switch (type) {
                case "user":
                    break;
                case "tickets":
                    accountType = 1;
                    break;
                default:
                    return "account/register";
            }


            entity = new AccountEntity(mail, password, accountType);
            System.err.println(mail + " " + password + " " + accountType);

            entity = accountRepository.save(entity);

            if (accountType == 0) {
                MemberEntity memberEntity = new MemberEntity();

                BeanUtils.copyProperties(entity, memberEntity);
                memberEntity.setName("name");
                memberEntity.setPhone("00000000000");
                memberEntity.setPoint(0);
                memberEntity.setDiscount(0);
                memberRepository.save(memberEntity);
                System.err.println(memberEntity.toString());
                sendTemplateMail(memberEntity.getEmail(), Integer.toString(memberEntity.getId()));
            } else if (accountType == 1) {

                VenueEntity venueEntity = new VenueEntity();

                BeanUtils.copyProperties(entity, venueEntity);
                venueEntity.setLocation("road number");
                venueEntity.setFsnumber(0);
                venueEntity.setBsnumber(0);

                venueEntity.setApprove(0);
                venueEntity.setName("name");
                venueEntity.setSystemid(randomString());
                venueRepository.save(venueEntity);
                System.err.println(venueEntity.toString());
            }


            return "redirect:/index";
        }
    }

    @RequestMapping({"/", "/login", "/index"})
    public String login() {
        return "account/index";
    }

    @RequestMapping("/register")
    public String register(SessionStatus sessionStatus) {
        sessionStatus.setComplete();
        return "account/register";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(SessionStatus sessionStatus) {

        sessionStatus.setComplete();
        return "account/index";
    }


    public void sendTemplateMail(String recipient, String userId) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(sender);
            helper.setTo(recipient);
            helper.setSubject("Tickets网验证邮件");
            Context context = new Context();
            context.setVariable("id", userId);
            String emailContent = templateEngine.process("emailTemplate", context);
            helper.setText(emailContent, true);
        } catch (MessagingException e) {
            throw new RuntimeException("Messaging  Exception !", e);
        }
        javaMailSender.send(message);
    }

    @RequestMapping(value = "activation/{userId}", method = RequestMethod.GET)
    public String activation(@PathVariable String userId, HttpServletResponse response) throws IOException {
        MemberEntity memberEntity = memberRepository.findOne(Integer.parseInt(userId));
        System.err.println(memberEntity);
        if (memberEntity != null) {
            memberEntity.setState(1);
            memberEntity.setQualification(1);

            memberRepository.save(memberEntity);
        }
        return "redirect:/index";
    }

    //验证是否是邮箱
    public static boolean isEmail(String string) {
        if (string == null)
            return false;
        String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p;
        Matcher m;
        p = Pattern.compile(regEx1);
        m = p.matcher(string);
        if (m.matches())
            return true;
        else
            return false;
    }

    //随机生成7位识别码
    public static String randomString() {
        String s = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random r = new Random();
        String result = "";
        for (int i = 0; i < 7; i++) {
            int n = r.nextInt(62);
            result += s.substring(n, n + 1);
        }
        return result;
    }
}
