package cl.banking.users.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users", indexes = { 
		@Index(name = "idx_email", columnList = "email", unique = true),
		@Index(name = "idx_token", columnList = "token")
})
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(updatable = false, nullable = false, columnDefinition = "UUID")
	private UUID id;
	
	@Column(nullable = false, length = 50)
	private String name;
	
	@Column(nullable = false, unique = true, length = 50)
	private String email;
	
	@Column(nullable = false, length = 255)
	private String password;
	
	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(
		name = "user_phones", 
		joinColumns = @JoinColumn(name = "user_id"),
		indexes = @Index(name = "idx_user_phones_user_id", columnList = "user_id")
	)
	@Builder.Default
	private List<Phone> phones = new ArrayList<>();
	
	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;
	
	@UpdateTimestamp
	@Column(nullable = false)
	private LocalDateTime modifiedAt;
	
	@Column(nullable = false)
	private LocalDateTime lastLogin;
	
	@Column(length = 512)
	private String token;
	
	@Column(nullable = false)
	private Boolean isActive; 
	
}
