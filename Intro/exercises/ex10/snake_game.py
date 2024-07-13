import math
from typing import Optional
from game_display import GameDisplay
import game_utils
from snake import Snake
from apples import Apple
from walls import Wall


class SnakeGame:
    """
    init - crate new game object
           __width - the board width(int)
           __height - the board height(int)
           __x, __y - the staring point of the snake(int)
           __key_clicked - the direction of the snake acording to user choise
           __apples_num - number of apples in the game(int)
           __apples - set of apples objects
           __walls_num - number of walls in the game(int)
           __walls - set of walls objects
           __debug - set if there will be a snake in the game for debuging(bool)
           __max_rounds - max rounds of the game if given(int)
           __round - number of current round
           __snake - snake object
           __is_over - bool type that determines if the game is over
           __score - the score(int)
    read_key - gets the new key_clicked by the user and update it
    update_objects - updating the move and interactions of the objects in the game
    __move_snake - move the snake acording to the direction
    __wall_on_snake - check if the snake tuch the wall
    __wall_on_apple - check if a wall above apple
    __move_walls - move all the walls acording to their direction
    __is_wall_in_board - check if the wall_cells inside the board
    __update_wall - adding and deleting walls if necessary
    __update_apple - adding apples if necessary
    __is_cell_empty - check if the cell is empty(bool)
    __snake_on_apple - check if the snake head is over apple
    __is_snake_in_board - check if the snake head is inside the board
    __draw_wall - draw the walls in blue on the board
    __draw_apple - draw thw apples in green on the board
    __draw_snake - draw the snake in black on the board
    draw_board - draw the board
    end_round - check if the round passed the max_round(if was given)
    is_over - return self__is_over(bool)
    """

    def __init__(self, height: int, width: int, apples: int, debug: bool, walls: int, rounds: int) -> None:
        self.__width = width
        self.__height = height
        self.__x = width // 2
        self.__y = height // 2
        self.__key_clicked = game_utils.UP
        self.__apples_num = apples
        self.__apples = set()
        self.__walls_num = walls
        self.__walls = set()
        self.__debug = debug
        self.__max_rounds = rounds
        self.__round = 0
        if debug == False:
            self.__snake = Snake((self.__x, self.__y))
        else:
            self.__snake = []
        self.__is_over = False
        self.__score = 0

    def read_key(self, key_clicked: Optional[str]) -> None:
        # gets the new key_clicked by the user and update it if valid
        if self.__key_clicked == game_utils.UP and key_clicked == game_utils.DOWN:
            pass
        elif self.__key_clicked == game_utils.DOWN and key_clicked == game_utils.UP:
            pass
        elif self.__key_clicked == game_utils.LEFT and key_clicked == game_utils.RIGHT:
            pass
        elif self.__key_clicked == game_utils.RIGHT and key_clicked == game_utils.LEFT:
            pass
        elif key_clicked == None:
            pass
        else:
            self.__key_clicked = key_clicked

    def round_zero(self):
        if self.__max_rounds == 0:
            self.__is_over = True
        else:
            self.__round += 1
            self.__update_wall()
            self.__update_apple()

    def update_objects(self) -> None:
        # move
        if self.__debug == False:
            self.__move_snake()
        if self.__round % 2 == 1:
            self.__move_walls()
        # interactions
        self.__wall_on_apple()
        if self.__debug == False:
            self.__wall_on_snake()
            self.__snake_on_apple()
        # updating
        self.__update_wall()
        self.__update_apple()

    def __move_snake(self):
        # check if out of borders
        in_board = self.__is_snake_in_board()
        if in_board:
            self.__snake.move_snake((self.__x, self.__y))
            # check if above itself
            if self.__snake.check_snake_above_tail():
                self.__is_over = True
        else:
            self.__snake.remove_tail()
            self.__is_over = True

    def __wall_on_snake(self):
        # create list of all walls locations
        loc_of_walls = []
        for wall in self.__walls:
            loc_of_walls.extend(wall.get_loc())
        for i in range(self.__snake.get_snake_len()):
            if self.__snake.get_snake_loc()[i] in loc_of_walls:
                if i < 2:  # hit the head of the snake
                    self.__is_over = True
                else:  # hit the body of the snake
                    self.__snake.future_cut(i)

    def __wall_on_apple(self):
        # create a list of all walls locations
        loc_of_walls = []
        for wall in self.__walls:
            loc_of_walls.extend(wall.get_loc())
        bye_apples = set()  # set of all apple are to be deleted
        for apple in self.__apples:
            if apple.get_loc() in loc_of_walls:
                bye_apples.add(apple)
        # remove apples that crossed by a wall
        for apple in bye_apples:
            self.__apples.remove(apple)

    def __move_walls(self):
        bye_walls = []  # list of all walls are to be deleted
        for wall in self.__walls:
            wall.move_wall()
            self.__is_wall_in_board(wall)
            if wall.get_loc() == []:  # check if all the wall outside the board
                bye_walls.append(wall)
        # remove the walls
        for wall in bye_walls:
            self.__walls.remove(wall)

    def __is_wall_in_board(self, wall):
        # check if the wall inside the board. if not delete the cells out of the board
        for index, wall_loc in enumerate(wall.get_loc()):
            if wall_loc[0] < 0 or wall_loc[1] < 0 or wall_loc[0] >= self.__width or wall_loc[1] >= self.__height:
                wall.delete_cell(index)
                break

    def __update_wall(self):
        if len(self.__walls) < self.__walls_num:
            check = True
            wall = Wall()
            for cell_wall in wall.get_loc():
                if not self.__is_cell_empty(cell_wall):
                    check = False
            if check:
                self.__walls.add(wall)

    def __update_apple(self):
        if len(self.__apples) < self.__apples_num:
            apple = Apple()
            if self.__is_cell_empty(apple.get_loc()):
                self.__apples.add(apple)

    def __is_cell_empty(self, cell):
        loc_on_board = []
        # add snake locations
        if self.__debug == False:
            loc_on_board.extend(self.__snake.get_snake_loc())
        # add wall locations
        for wall in self.__walls:
            loc_on_board.extend(wall.get_loc())
        # add apple locations
        for apple in self.__apples:
            loc_on_board.append(apple.get_loc())
        # check if the given cell is occupied cell
        if cell in loc_on_board:
            return False
        return True

    def __snake_on_apple(self):
        snake_head = (self.__x, self.__y)
        flag = 0
        # check if snake head is over apple
        for apple in self.__apples:
            if snake_head == apple.get_loc():
                flag = 1
                break
        # snake head is over apple
        if flag == 1:
            self.__snake.add_additional_cells()
            self.__apples.remove(apple)
            # update score
            add_score = math.floor(
                math.sqrt(self.__snake.get_snake_len()))
            self.__score += add_score

    def __is_snake_in_board(self):
        # check if the future cell is inside the board. if not delete the snake head and is_over = True
        if (self.__key_clicked == game_utils.LEFT) and (self.__x > 0):
            self.__x -= 1
            return True
        elif (self.__key_clicked == game_utils.RIGHT) and (self.__x < self.__width-1):
            self.__x += 1
            return True
        elif (self.__key_clicked == game_utils.DOWN) and (self.__y > 0):
            self.__y -= 1
            return True
        elif (self.__key_clicked == game_utils.UP) and (self.__y < self.__height-1):
            self.__y += 1
            return True
        return False

    def __draw_wall(self, gd: GameDisplay):
        for wall in self.__walls:
            for cell_wall in wall.get_loc():
                # check if the cell isdide the board
                if cell_wall[0] < 0 or cell_wall[1] < 0 or cell_wall[0] >= self.__width or cell_wall[1] >= self.__height:
                    pass
                else:
                    gd.draw_cell(cell_wall[0], cell_wall[1], "blue")

    def __draw_apple(self, gd: GameDisplay):
        for apple in self.__apples:
            apple_loc = apple.get_loc()
            gd.draw_cell(apple_loc[0], apple_loc[1], "green")

    def __draw_snake(self, gd: GameDisplay):
        for loc in self.__snake.get_snake_loc():
            gd.draw_cell(loc[0], loc[1], "black")

    def draw_board(self, gd: GameDisplay) -> None:
        self.__draw_apple(gd)
        if self.__debug == False:
            self.__draw_snake(gd)
        self.__draw_wall(gd)
        gd.show_score(self.__score)

    def end_round(self) -> None:
        if self.__round == self.__max_rounds:
            self.__is_over = True
        self.__round += 1

    def is_over(self) -> bool:
        return self.__is_over
