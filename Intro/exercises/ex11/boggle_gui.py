import tkinter
import random

CORRECT_WORD_LIST = ["expensive brother you are!", "!you are the king!", "AmAzInG", "GGWP", "can you tech me english plz?!",
                     "WOW", "noice", "great!", "cool cool cool cool", "ya melech", "ya gever/et!", "ya gaon/a"]
UNCORRECT_WORD_LIST = ["I dont like this game anymore", "nice try", "try again", "adif shetiftosh", "remember you play in english",
                       "its not over! you still have time", "my littel brother is better then you", "you are not very good ha?"]

FONT = 'Helvetica'
REGULAR_COLOUR = "grey"
PRESS_COLOR = "blue"
BUTTON_ACTIVE_COLOR = "red"
BUTTON_STYLE = {"font": ("Helvetica", 30), "borderwidth": 3, "relief": tkinter.RAISED,
                "bg": REGULAR_COLOUR, "activebackground": BUTTON_ACTIVE_COLOR, "width": 4, "height": 2}


class Boggle_gui:
    """
    push_start_button - start the game (set game screen and time)
    set_button_board_cmd - set cmd for a button board
    set_anoter_game_cmd - set cmd for the anoter game button
    set_time_over_cmd - set cmd to anoter game button when created
    destroy_main_loop - close the window
    set_start_button_cmd - set cmd for the start button
    start - start the game
    get_board_button_list - return a list of tuples represent button char and location
    get_button_board_color - return the color of a button acoording to a given index
    reset_board_buttons_color - reset all the board buttons colors to regular color
    set_button_board_color - set the color of button acoording to a given index
    update_found_words - update the found words labels on the side
    add_letter_to_word - add a letter to the current word label
    reset_word - reset the label word
    update_score - update the label score
    """

    def __init__(self, chars_lst: list[str]) -> None:
        root = tkinter.Tk()
        root.title("Boggle War 2.0")
        root.resizable(False, False)
        self.__main_window = root
        self.__outer_frame = tkinter.Frame(root)
        self.__outer_frame.pack(side=tkinter.TOP)
        self.__create_upper_frame()
        self.__create_center_frame()
        self.__create_lower_frame()
        self.__buttons_char_lst = chars_lst
        self.__time_over_cmd = None

    def push_start_button(self):
        """starts the game - 
            sets all arguments 
            sets all gui objects
            start clock"""
        self.__start_button.destroy()  # remove the start screen
        # create the game screen
        self.__fun_label = tkinter.Label(
            self.__lower_frame, font=(FONT, 10), width=5)
        self.__fun_label.pack()
        self.__score_show["text"] = '0'
        self.__time = 180
        self.__char_on_buttens()
        self.__feedback_label = tkinter.Label(
            self.__lower_frame, font=(FONT, 20))
        self.__feedback_label.pack(side=tkinter.TOP)
        self.__create_apply_word_button()
        self.__start_clock()
        self.__current_word["text"] = ""
        self.__found_words_side = "R"  # represent the next side a new word will b added

    def __create_apply_word_button(self):
        self.__word_button = tkinter.Button(
            self.__lower_frame, text="APPLY WORD", font=("Helvetica", 15), bg="white")
        self.__word_button.pack()

    def __char_on_buttens(self):
        """set the chars list to each board button"""
        for i in range(len(self.__buttons_char_lst)):
            self.__board_buttons[i]["text"] = self.__buttons_char_lst[i]

    def set_button_board_cmd(self, button_index: int, cmd):
        self.__board_buttons[button_index]["command"] = cmd

    def set_button_apply_word_cmd(self, cmd):
        self.__word_button["command"] = cmd

    def set_anoter_game_cmd(self, cmd):
        self.__time_over_cmd = cmd

    def set_time_over_cmd(self, cmd):
        self.__time_over_cmd["command"] = cmd

    def __start_clock(self):
        """
        start the clock
        if finish - initiate finish game command
        """
        min = str(self.__time // 60)
        sec = self.__time % 60
        if sec < 10:
            sec = "0"+str(sec)
        else:
            sec = str(sec)
        if int(min) < 0 or int(sec) < 0:
            self.__finish_game()
        else:
            self.__time_show["text"] = str(min) + ":" + str(sec)
            self.__time -= 1
            self.__main_window.after(1000, self.__start_clock)

    def __finish_game(self):
        """create the finish game screen"""
        score = self.__score_show["text"]
        self.__outer_frame.destroy()
        self.__create_finish_frame(score)

    def __create_finish_frame(self, score: str):
        self.__outer_frame = tkinter.Frame(
            self.__main_window)
        self.__outer_frame.pack(side=tkinter.TOP)
        self.__finish_label_score = tkinter.Label(
            self.__outer_frame, font=(FONT, 20, "bold"), width=50, height=5, text="YOUR SCORE: "+score)
        self.__finish_label_score.pack(side=tkinter.TOP)
        self.__finish_button = tkinter.Button(self.__outer_frame,
                                              text="ANOTHER GAME?", font=("Helvetica", 30), borderwidth=3, relief=tkinter.RAISED,
                                              bg=REGULAR_COLOUR, activebackground=BUTTON_ACTIVE_COLOR, command=self.__time_over_cmd)
        self.__finish_button.pack(side=tkinter.TOP)

    def destroy_main_loop(self):
        self.__main_window.destroy()

    def __create_lower_frame(self):
        """
        button to start the game
        """
        self.__lower_frame = tkinter.Frame(self.__outer_frame)
        self.__lower_frame.pack(side=tkinter.TOP)
        self.__start_button = tkinter.Button(self.__lower_frame,
                                             **BUTTON_STYLE, text="START")
        self.__start_button.pack()

    def set_start_button_cmd(self, cmd):
        self.__start_button["command"] = cmd

    def __create_center_frame(self):
        # center frame
        self.__center_frame = tkinter.Frame(self.__outer_frame)
        self.__center_frame.pack(side=tkinter.TOP)
        # center left frame - words found left
        self.__center_left_frame = tkinter.Frame(self.__center_frame)
        self.__center_left_frame.pack(side=tkinter.LEFT)
        self.__left_found_words = tkinter.Label(
            self.__center_left_frame, font=(FONT, 20, 'bold'), width=5)
        self.__left_found_words.pack(side=tkinter.TOP)
        # center right frame - words found right
        self.__center_right_frame = tkinter.Frame(
            self.__center_frame)
        self.__center_right_frame.pack(side=tkinter.RIGHT)
        self.__right_found_words = tkinter.Label(
            self.__center_right_frame, font=(FONT, 20, 'bold'), width=5)
        self.__right_found_words.pack(side=tkinter.TOP)
        # center middle frame - the board
        self.__center_middle_frame = tkinter.Frame(self.__center_frame)
        self.__center_middle_frame.pack()
        self.__board_buttons = []
        self.__crate_buttons_in_center_frame()

    def __crate_buttons_in_center_frame(self):
        for i in range(4):
            tkinter.Grid.columnconfigure(
                self.__center_middle_frame, i, weight=1)
        for i in range(4):
            tkinter.Grid.rowconfigure(
                self.__center_middle_frame, i, weight=1)
        for i in range(4):
            for j in range(4):
                self.__make_button(i, j)

    def __make_button(self, row_button, col_button):
        """create buttons before the game start"""
        button = tkinter.Button(
            self.__center_middle_frame, text="?", **BUTTON_STYLE)
        button.grid(row=row_button, column=col_button, sticky=tkinter.NSEW)
        self.__board_buttons.append(button)

    def __create_upper_frame(self):
        # upper frame
        self.__upper_frame = tkinter.Frame(self.__outer_frame)
        self.__upper_frame.pack(side=tkinter.TOP)
        # score frame
        self.__score_frame = tkinter.Frame(self.__upper_frame)
        self.__score_frame.pack(side=tkinter.LEFT)
        self.__score_title = tkinter.Label(
            self.__score_frame, font=(FONT, 20, "bold"), width=10, text="SCORE")
        self.__score_title.pack(side=tkinter.TOP)
        self.__score_show = tkinter.Label(
            self.__score_frame, font=(FONT, 20, "bold"), width=10, relief="sunken")
        self.__score_show.pack(side=tkinter.BOTTOM)
        # word frame
        self.__current_word = tkinter.Label(
            self.__upper_frame, font=(FONT, 20, "bold"), width=13, text="WORD")
        self.__current_word.pack(side=tkinter.LEFT)
        # time frame
        self.__time_frame = tkinter.Frame(self.__upper_frame)
        self.__time_frame.pack(side=tkinter.RIGHT)
        self.__time_title = tkinter.Label(
            self.__time_frame, font=(FONT, 20, "bold"), width=10, text="TIME")
        self.__time_title.pack(side=tkinter.TOP)
        self.__time_show = tkinter.Label(
            self.__time_frame, font=(FONT, 20, "bold"), width=10, relief="sunken")
        self.__time_show.pack(side=tkinter.BOTTOM)

    def start(self):
        self.__main_window.mainloop()

    def get_board_button_list(self):
        """return a list of tuples represent button char and location"""
        tuple_button_list = []
        for i in range(len(self.__board_buttons)):
            tuple_button_list.append(
                (self.__board_buttons[i]["text"], i))
        return tuple_button_list

    def get_button_board_color(self, index_button):
        return self.__board_buttons[index_button]["bg"]

    def reset_board_buttons_color(self):
        for i in range(len(self.__board_buttons)):
            self.set_button_board_color(i, REGULAR_COLOUR)

    def set_button_board_color(self, index_button: int, color: str):
        self.__board_buttons[index_button]["bg"] = color

    def update_found_words(self, word: str):
        if self.__found_words_side == "R":
            self.__right_found_words["text"] += word + '\n'
            self.__found_words_side = "L"
        else:
            self.__left_found_words["text"] += word + '\n'
            self.__found_words_side = "R"

    def add_letter_to_word(self, char):
        self.__current_word["text"] += char

    def reset_word(self):
        self.__current_word["text"] = ""

    def update_score(self, score: int):
        self.__score_show["text"] = str(score)

    def correct_word(self):
        feedback = random.choice(CORRECT_WORD_LIST)
        self.__feedback_label["text"] = feedback

    def uncorrect_word(self):
        feedback = random.choice(UNCORRECT_WORD_LIST)
        self.__feedback_label["text"] = feedback
