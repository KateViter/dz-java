package dz_16.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDatesBody {
    private String checkin;
    private String checkout;
}
