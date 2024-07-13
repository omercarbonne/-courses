
// don't change those includes
#include "User.h"
#include "RecommendationSystem.h"


// implement your cpp code here
User::User(std::string name, rank_map ranks, rec_sys_ptr rec_ptr) {
    _name = name;
    _ranks = ranks;
    _rec_ptr = rec_ptr;
}

const std::string &User::get_name() const {return _name;}

void User::add_movie_to_rs(const std::string &name, int year,
                           const std::vector<double> &features, double rate)
{
    sp_movie movie_ptr = _rec_ptr->add_movie(name, year, features);
    _ranks[movie_ptr] = rate;
}

sp_movie User::get_recommendation_by_content() const {
    return _rec_ptr->recommend_by_content(*this);
}

sp_movie User::get_recommendation_by_cf(int k) const
{
    return _rec_ptr->recommend_by_cf(*this, k);
}

double User::get_prediction_score_for_movie(const std::string
    &name, int year, int k) const {
    sp_movie movie = _rec_ptr->get_movie(name, year);
    if(movie != nullptr)
    {
        return _rec_ptr->predict_movie_score(*this, movie, k);
    }
    return 0.0;
}

std::ostream& operator<<(std::ostream &os, const User &user)
{
    os << "name: " << user._name << std::endl << (*user._rec_ptr);
    return os;
}

const rank_map &User::get_ranks() const {return _ranks;}

