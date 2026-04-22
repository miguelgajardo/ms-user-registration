package cl.banking.users.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Phone {
	
	@Column(nullable = false, length = 15)
	private String number;
	
	@Column(nullable = false, length = 5)
	private String cityCode;
	
	@Column(nullable = false, length = 4)
	private String countryCode;

}