package rcs.stockapp.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import rcs.stockapp.models.UserStocks;

public interface UserStocksRepository extends MongoRepository<UserStocks, String>, UserStocksRepositoryCustom {
}
