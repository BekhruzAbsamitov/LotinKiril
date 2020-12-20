import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MyBot extends TelegramLongPollingBot {

    static String lotindanKirilga = "Lotindan Kirilga";
    static String kirildanLotinga = "Kirildan Lotinga";

    static String[] cyrillicAlphabet = {
            "Ў", "ў", "Ғ", "ғ", "Ё", "Ц", "Ч", "Ш",
            "Я", "Я", "я", "Ю", "ю", "Ш", "ш", "Ч",
            "ч", "Ц", "ц", "Ё", "ё", "А", "а", "Б",
            "б", "В", "в", "Г", "г", "Д", "д", "Е",
            "е", "Ж", "ж", "З", "з", "И", "и", "Й",
            "й", "К", "к", "Л", "л", "М", "м", "Н",
            "н", "О", "о", "П", "п", "Р", "р", "С",
            "с", "Т", "т", "У", "у", "Ф", "ф", "Х",
            "х", "Э", "э", "Қ", "қ", "Ҳ", "ҳ", ".",
            ",", " ", "?", "!"
    };

    static String[] latinAlphabet = {
            "O'", "o'", "G'", "g'", "Yo", "Ts", "Ch",
            "Sh", "Ya", "YA", "ya", "Yu", "yu", "Sh",
            "sh", "CH", "ch", "TS", "ts", "YO", "yo",
            "А", "a", "B", "b", "V", "v", "G", "g",
            "D", "d", "E", "e", "J", "j", "Z", "z",
            "I", "i", "Y", "y", "K", "k", "L", "l",
            "M", "m", "N", "n", "O", "o", "P", "p",
            "R", "r", "S", "s", "T", "t", "U", "u",
            "F", "f", "H", "h", "E", "e", "Q", "q",
            "X", "x", ".", ",", " ", "?", "!"
    };
    static Scanner scanner = new Scanner(System.in);

    static boolean isLatin = false;

    @Override
    public void onUpdateReceived(Update update) {
        String text = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();
        SendMessage msg = new SendMessage().setChatId(chatId);
        String firstName = update.getMessage().getChat().getFirstName();


        if (update.hasMessage() && update.getMessage().hasText()) {
            if (text.equals("/start")) {
                String str = "Salom " + firstName + "!" + "\n\nQuyidagilardan birini tanlang:";
                msg.setText(str).setReplyMarkup(getKeyboard());
                executeMsg(msg);
            } else if (text.equals(lotindanKirilga)) {
                isLatin = true;
                msg.setText("Lotin matnini kiriting:");
                executeMsg(msg);
            } else if (text.equals(kirildanLotinga)) {
                isLatin = false;
                msg.setText("Kiril matnini kiriting:");
                executeMsg(msg);
            } else {
                if (isLatin) {
                    if (convertFromLatinToCyrillic(text).length() != 0) {
                        msg.setText(convertFromLatinToCyrillic(text));
                    } else {
                        msg.setText("Lotin matn kiriting");
                    }
                } else {
                    if (convertFromCyrillicToLatin(text).length() != 0) {
                        msg.setText(convertFromCyrillicToLatin(text));
                    } else {
                        msg.setText("Lotin matn kiriting");
                    }
                }
                executeMsg(msg);
            }
        }

    }

    static String convertFromLatinToCyrillic(String message) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            for (int j = 0; j < latinAlphabet.length; j++) {
                if (i == message.length() - 1) {
                    if (latinAlphabet[j]
                            .equals(String.valueOf(message.charAt(i)))) {
                        str.append(cyrillicAlphabet[j]);
                    }

                } else {
                    if (latinAlphabet[j].equals(String.valueOf(message.charAt(i))
                            .concat(String.valueOf(message.charAt(i + 1))))) {
                        str.append(cyrillicAlphabet[j]);
                        i++;
                        break;
                    }
                    if (latinAlphabet[j].equals(String.valueOf(message.charAt(i)))) {
                        str.append(cyrillicAlphabet[j]);
                        break;
                    }
                }
            }

        }
        return str.toString();

    }

    static String convertFromCyrillicToLatin(String message) {
        StringBuilder s = new StringBuilder();
        for (int j = 0; j < message.length(); j++) {
            for (int k = 0; k < cyrillicAlphabet.length; k++) {
                if (String.valueOf(message.charAt(j))
                        .equals(cyrillicAlphabet[k])) {
                    s.append(latinAlphabet[k]);
                    break;
                }
            }
        }
        return s.toString();
    }

    public ReplyKeyboardMarkup getKeyboard() {

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        replyKeyboardMarkup.setSelective(true);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();

        KeyboardButton keyboardButton1 = new KeyboardButton();
        KeyboardButton keyboardButton2 = new KeyboardButton();

        KeyboardRow keyboardRow1 = new KeyboardRow();
        KeyboardRow keyboardRow2 = new KeyboardRow();

        keyboardButton1.setText(lotindanKirilga);
        keyboardRow1.add(keyboardButton1);

        keyboardButton2.setText(kirildanLotinga);
        keyboardRow2.add(keyboardButton2);

        keyboardRowList.add(keyboardRow1);
        keyboardRowList.add(keyboardRow2);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);

        return replyKeyboardMarkup;
    }

    public void executeMsg(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "lotin_kiril_1_bot";
    }

    @Override
    public String getBotToken() {
        return "1450576056:AAHH8LO21IBYq-nGID7jrJZ3QrlRXS-CZLs";
    }
}