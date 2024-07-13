#include "RecommendationSystem.h"


RecommendationSystem::RecommendationSystem() {}

sp_movie RecommendationSystem::recommend_by_content(const User &user)
{
    std::vector<double> pref_vec = get_pref_vector(user);
    double max = -1;
    sp_movie best_movie;
    rank_map user_ranks = user.get_ranks();
    for (auto it = _movies_map.begin(); it != _movies_map.end(); it++)
    {
        if(user_ranks.find(it->first) == user_ranks.end())
        { // if the user didnt watch the movie
            double rate = get_angel(it->second, pref_vec);
            if(rate > max)
            {
                max = rate;
                best_movie = it->first;
            }
        }
    }
    return best_movie;
}

double RecommendationSystem::predict_movie_score(const User &user,
                                                 const sp_movie &movie, int k)
{
    std::set<std::pair<sp_movie, double>, set_compare> similarity;
    // so we can take the the top k
    rank_map user_ranks = user.get_ranks();
    std::vector<double> unwatched_movie_vector =
            _movies_map.find(movie)->second;
    for (auto it = user_ranks.begin(); it != user_ranks.end(); it++)
    { // calc the similarity rate for all the movies the user rated
        //iterates over all the movies the user rated.
        std::vector<double> watched_movie_vector =
                _movies_map.find(it->first)->second;
        double rate = get_angel(watched_movie_vector,unwatched_movie_vector);
        similarity.insert({it->first, rate});
    }
    //now calc the predicted rate of the user
    double sum_top = 0;
    double sum_bottom = 0;
    int count = 0;
    for (auto it = similarity.rbegin(); it != similarity.rend(); it++)
    {
        std::pair<sp_movie, double> watched_movie = *it;
        sum_top += watched_movie.second * (user_ranks[watched_movie.first]);
        sum_bottom += watched_movie.second;
        if(++count >= k) { break; }
    }
    return sum_top/sum_bottom;
}

sp_movie RecommendationSystem::recommend_by_cf(const User &user, int k)
{
    double max = 0;
    sp_movie best_movie;
    rank_map user_ranks = user.get_ranks();
    for (auto it = _movies_map.begin(); it != _movies_map.end(); it++)
    {
        if(user_ranks.find(it->first) == user_ranks.end())
        { // if the user didnt watch the movie
            double rate = predict_movie_score(user, it->first, k);
            if(rate > max)
            {
                max = rate;
                best_movie = it->first;
            }
        }
    }
    return best_movie;
}

sp_movie RecommendationSystem::add_movie(const std::string &name, int year
                                         , const std::vector<double> &features)
{
    sp_movie new_movie = std::make_shared<Movie>(name, year);
    std::vector<double> new_vector = features;
    _movies_map[new_movie] = new_vector;
    return new_movie;
}

sp_movie RecommendationSystem::get_movie
(const std::string &name, int year) const
{
    sp_movie movie = std::make_shared<Movie>(name, year);
    movies_map::const_iterator it = _movies_map.find(movie);
    if(it != _movies_map.end())
    {
        return it->first;
    }
    return nullptr;
}

std::ostream& operator<<(std::ostream &os, RecommendationSystem &rec_sys)
{
    for(std::pair<sp_movie, std::vector<double>> movie: rec_sys._movies_map) {
        os << (*movie.first);
    }
    return os;
}

rank_map& RecommendationSystem::normalize
(rank_map &ranks, double average) const{
    for(auto &pair: ranks)
    {
        pair.second -= average;
    }
    return ranks;
}

double RecommendationSystem::get_average(const User &user) const
{
    rank_map ranks = user.get_ranks();
    double sum = 0;
    int count = 0;
    for(std::pair<sp_movie, double> pair: ranks)
    {
        count++;
        sum += pair.second;
    }
    return sum/count;
}


std::vector<double> RecommendationSystem::get_pref_vector(const User &user)
{
    double average = get_average(user);
    rank_map ranks = user.get_ranks(); // a copy of the user ranking map.
    normalize(ranks,average); // normalize the ranks map.
    int size = _movies_map.begin()->second.size();
    std::vector<double> vec(size); // initialized by default to 0;
    std::vector<double> movie_features;
    for(auto it = ranks.begin(); it != ranks.end(); it++)
    { // iterates over all the movies in the users ranks map.
        movie_features = _movies_map.find(it->first)->second;
        for (int i = 0; i < size; i++)
        { // updates the pref vector
            vec[i] += (movie_features[i]*it->second);
        }
    }
    return vec;
}

std::vector<double> RecommendationSystem::
add(const std::vector<double> &vec1,
    const std::vector<double> &vec2) const
{
    std::vector<double> new_vector;
    for(size_t i = 0; i < _movies_map.begin()->second.size(); i++)
    {
        new_vector.push_back(vec1[i] + vec2[i]);
    }
    return new_vector;
}

double RecommendationSystem::mult(const std::vector<double> &vec1,
                                  const std::vector<double> &vec2) const
{
    double sum = 0;
    for(size_t i = 0; i < vec1.size(); i++)
    {
        sum += vec1[i]*vec2[i];
    }
    return sum;
}

double RecommendationSystem::get_angel(const std::vector<double> &vec1,
                                       const std::vector<double> &vec2)
{
    double top = mult(vec1, vec2);
    double bottom = std::sqrt(mult(vec1, vec1))*
            std::sqrt(mult(vec2, vec2));
    return top/bottom;
}