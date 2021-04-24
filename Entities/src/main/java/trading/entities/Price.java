package trading.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Price
{
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private int id;
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

