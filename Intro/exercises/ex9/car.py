class Car:
    """
    Add class description here
    """
    def __init__(self, name, length, location, orientation):
        """
        A constructor for a Car object
        :param name: A string representing the car's name
        :param length: A positive int representing the car's length.
        :param location: A tuple representing the car's head (row, col) location
        :param orientation: One of either 0 (VERTICAL) or 1 (HORIZONTAL)
        """
        self.__name = name
        self.__length = length
        self.__location = location
        self.__orientation = orientation

    def car_coordinates(self):
        """
        :return: A list of coordinates the car is in
        """
        coordinates = []
        for i in range(self.__length):
            if self.__orientation == 0:
                coordinates.append((self.__location[0] + i, self.__location[1]))
            else:
                coordinates.append((self.__location[0], self.__location[1] + i))
        return coordinates

    def possible_moves(self):
        """
        :return: A dictionary of strings describing possible movements permitted by this car.
        """
        if self.__orientation == 1:
            result = {'u': " cause the car to move up", 'd': "cause the car to move down"}
        else:
            result = {'r': " cause the car to move right", 'l': "cause the car to move left"}
        return result

    def movement_requirements(self, move_key):
        """ 
        :param move_key: A string representing the key of the required move.
        :return: A list of cell locations which must be empty in order for this move to be legal.
        """
        if move_key == 'd':
            return [(self.__location[0] + self.__length, self.__location[1])]
        elif move_key == 'u':
            return [(self.__location[0] - 1, self.__location[1])]
        elif move_key == 'r':
            return [(self.__location[0], self.__location[1] + self.__length)]
        elif move_key == 'l':
            return [(self.__location[0], self.__location[1]-1)]

    def move(self, move_key):
        """ 
        :param move_key: A string representing the key of the required move.
        :return: True upon success, False otherwise
        """
        if self.__orientation == 0:
            if move_key == 'd':
                self.__location = (self.__location[0] + 1, self.__location[1])
                return True
            elif move_key == 'u':
                self.__location = (self.__location[0] - 1, self.__location[1])
                return True
        else:
            if move_key == 'r':
                self.__location = (self.__location[0], self.__location[1] + 1)
                return True
            elif move_key == 'l':
                self.__location = (self.__location[0], self.__location[1] - 1)
                return True
        return False  # if nothing returned so far that's mean the movement wasn't legal

    def get_name(self):
        """
        :return: The name of this car.
        """
        return self.__name
