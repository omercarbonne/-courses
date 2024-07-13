class Board:
    """
    Add a class description here.
    Write briefly about the purpose of the class
    """

    def __init__(self):
        self.__board = [['' for i in range(7)] for j in range(7)]
        self.__board[3].append('E')  # adding the exit point, marked as 'E'
        self.__target_cell = (3,7)
        self.__cars = []

    def __str__(self):
        """
        This function is called when a board object is to be printed.
        :return: A string of the current status of the board
        """
        str_board = ''
        for row in self.__board:
            str_board += '\n'
            part_board = ''
            for col in row:
                if col == '':
                    part_board += ' _ '
                else:
                    part_board += ' ' + col + ' '
            str_board += part_board.strip()
        return str_board.strip()

    def cell_list(self):
        """ This function returns the coordinates of cells in this board
        :return: list of coordinates
        """
        cell_lst = []
        for i in range(7):
            for j in range(7):
                cell_lst.append((i, j))
        cell_lst.append(self.__target_cell)
        return cell_lst

    def possible_moves(self):
        """ This function returns the legal moves of all cars in this board
        :return: list of tuples of the form (name,move_key,description)
                 representing legal moves
        """
        poss_moves = []
        for car in self.__cars:
            name = car.get_name()
            for move, description in car.possible_moves().items():
                poss_moves.append((name, move, description))
        return poss_moves

    def target_location(self):
        """
        This function returns the coordinates of the location which is to be filled for victory.
        :return: (row,col) of goal location
        """
        return self.__target_cell

    def cell_content(self, coordinate):
        """
        Checks if the given coordinates are empty.
        :param coordinate: tuple of (row,col) of the coordinate to check
        :return: The name if the car in coordinate, None if empty
        """
        row, col = coordinate[0], coordinate[1]
        if self.__board[row][col] == 'E':  # if the coordinate is the exit point
            return None
        elif self.__board[row][col]:  # if the location is not marked as '', which is empty
            return self.__board[row][col]
        return None  # if not empty, contains the name of the car in it

    def __coordinate_inside_board(self, coordinate):
        row, col  = coordinate
        if 0 <= row < 7 and 0 <= col < 7:
            return True
        elif coordinate == self.__target_cell:
            return True
        else:
            return False

    def __is_car_location_legal(self, car):
        """
        this function checks if a given Car object is in legal location in the board.
        checks that all the car inside the board, and that it does not crash another car.
        :param car:
        :return: True/False if the location is legal
        """
        for coordinate in car.car_coordinates():
            row, col = coordinate[0], coordinate[1]
            if not self.__coordinate_inside_board(coordinate):  # checks the coordinate is in the board
                return False
            elif self.__board[row][col] != '':  # means that there is a car in that location
                return False
        return True  # if nothing returned yet it means all the coordinates are ok

    def __is_name_exist(self, name):
        """
        the function checks if a given name exist in the board
        :param name: name of a car
        :return: True/False if the name exist
        """
        for car in self.__cars:
            if car.get_name() == name:
                return True
        return False

    def add_car(self, car):
        """
        Adds a car to the game.
        :param car: car object of car to add
        :return: True upon success. False if failed
        """
        if self.__is_car_location_legal(car) and not self.__is_name_exist(car.get_name()):
            name = car.get_name()
            for coordinate in car.car_coordinates():
                row, col = coordinate[0], coordinate[1]
                self.__board[row][col] = name
            self.__cars.append(car)
            return True
        return False

    def __get_car_by_name(self, name):
        """
        this function gets the Car object according to a given name
        :param name:
        :return: Car, or None if there is not a matching car.
        """
        car = None
        for c in self.__cars:  # finds the required car
            if c.get_name() == name:
                car = c
        return car

    def __is_valid_move(self, name, move_key):
        """
        the functions checks the movvement is valid according to the board rules.
        :param name:
        :param move_key:
        :return: True if ok, False if not
        """
        car = self.__get_car_by_name(name)
        if car is None:  # if the car name does not exist
            return False
        coordinate = car.movement_requirements(move_key)[0]
        if self.__coordinate_inside_board(coordinate):
            if self.cell_content(coordinate) is None:  # which means the cell is empty
                return True
        return False

    def move_car(self, name, move_key):
        """
        moves car one step in given direction.
        :param name: name of the car to move
        :param move_key: Key of move in car to activate
        :return: True upon success, False otherwise
        """
        result = False
        if self.__is_valid_move(name, move_key):
            car = self.__get_car_by_name(name)
            coordinates = car.car_coordinates()  # coordinates before change
            result = car.move(move_key)
            if result: #  if the car movement succeed
                for coordinate in coordinates:  # erasing the old car location from the board
                    row, col = coordinate[0], coordinate[1]
                    self.__board[row][col] = ''
                coordinates = car.car_coordinates()  # updated coordinates
                for coordinate in coordinates:  # update the board with the new car location
                    row, col = coordinate
                    self.__board[row][col] = name
        return result

