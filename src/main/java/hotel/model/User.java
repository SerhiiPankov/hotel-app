package hotel.model;

import hotel.model.enums.Role;
import java.util.Arrays;
import java.util.Objects;

public class User {
    private long id;
    private String email;
    private String password;
    private byte[] salt;
    private String name;
    private String phone;
    private Role role;
    private String language;

    private User(Builder builder) {
        this.id = builder.id;
        this.email = builder.email;
        this.password = builder.password;
        this.salt = builder.salt;
        this.name = builder.name;
        this.phone = builder.phone;
        this.role = builder.role;
        this.language = builder.language;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    public String getPhone() {
        return phone;
    }

    public String getLanguage() {
        return language;
    }

    @Override
    public String toString() {
        return "User{"
                + "id=" + id
                + ", email='" + email + '\''
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return id == user.id
                && Objects.equals(email, user.email)
                && Objects.equals(password, user.password)
                && Arrays.equals(salt, user.salt)
                && Objects.equals(name, user.name)
                && Objects.equals(phone, user.phone)
                && role == user.role
                && Objects.equals(language, user.language);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, email, password, name, phone, role, language);
        result = 31 * result + Arrays.hashCode(salt);
        return result;
    }

    public static class Builder {
        private long id;
        private final String email;
        private String password;
        private byte[] salt;
        private String name;
        private String phone;
        private Role role;
        private String language;

        public Builder(String email) {
            this.email = email;
        }

        public User build() {
            return new User(this);
        }

        public Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setSalt(byte[] salt) {
            this.salt = salt;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder setRole(Role role) {
            this.role = role;
            return this;
        }

        public Builder setLanguage(String language) {
            this.language = language;
            return this;
        }
    }
}
