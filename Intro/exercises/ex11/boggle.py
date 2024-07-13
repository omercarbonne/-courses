import game_mind
import boggle_gui


class Boggle:

    def __init__(self) -> None:
        self.__mind = game_mind.BoggleModel()
        self.__chars_lst = self.__mind.get_char_on_board_lst()
        self.__gui = boggle_gui.Boggle_gui(self.__chars_lst)
        self.__gui.set_start_button_cmd(self.__create_start_button_cmd())

    def start(self):
        self.__gui.start()

    def __create_start_button_cmd(self):
        """create func for the start button"""
        def func():
            self.__gui.push_start_button()
            # create commands for buttons on board
            for button in self.__gui.get_board_button_list():
                cmd = self.__create_button_board_cmd(button)
                self.__gui.set_button_board_cmd(button[1], cmd)
            # create command for apply word button
            self.__gui.set_button_apply_word_cmd(
                self.__create_apply_button_cmd())
            # create command for future another game button
            self.__gui.set_anoter_game_cmd(
                self.__create_anoter_game_button_cmd())
        return func

    def __create_button_board_cmd(self, button):
        """create func for board buttons"""
        def func():
            loc = (button[1]//4, button[1] % 4)
            char = button[0]
            if self.__mind.is_neighbour(loc):
                if self.__gui.get_button_board_color(button[1]) == boggle_gui.REGULAR_COLOUR:
                    self.__mind.update_word_and_path(char, loc)
                    self.__gui.set_button_board_color(
                        button[1], boggle_gui.PRESS_COLOR)
                    self.__gui.add_letter_to_word(char)
        return func

    def __create_apply_button_cmd(self):
        """create func for apply word button"""
        def func():
            # mind
            if self.__mind.check_word():
                self.__mind.apply_word()
                # gui
                self.__gui.update_score(self.__mind.get_score())
                self.__gui.update_found_words(self.__mind.get_word())
                self.__gui.correct_word()
            else:
                self.__gui.uncorrect_word()
            # reset for new word
            self.__gui.reset_board_buttons_color()
            self.__gui.reset_word()
            self.__mind.reset_word_and_path()
        return func

    def __create_anoter_game_button_cmd(self):
        """create func for anoter game button"""
        def func():
            # close old game
            self.__gui.destroy_main_loop()
            # create new game
            self.__mind = game_mind.BoggleModel()
            self.__chars_lst = self.__mind.get_char_on_board_lst()
            self.__gui = boggle_gui.Boggle_gui(self.__chars_lst)
            self.__gui.set_start_button_cmd(self.__create_start_button_cmd())
            # start the game
            self.__gui.start()
        return func


if __name__ == '__main__':
    game = Boggle()
    game.start()
