package trading.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="price")
@Data
public class Price
{
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Long id;
    @Column(name="instrument_name", nullable=false, unique=false)
    private String instrumentName;
    @Column(name="timestamp", nullable=false, unique=false)
    @JsonFormat(pattern = "dd-MM-yyyy-HH:mm:ss:SSS")
    private LocalDateTime timestamp;
    @Column(name="bid", nullable=false, unique=false)
    private Double bid;
    @Column(name="ask", nullable=false, unique=false)
    private Double ask;
}

