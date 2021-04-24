package trading.repositories;

import trading.entities.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PricesRepository extends JpaRepository<Price, Long>
{
    public List<Price>findAll();
}
