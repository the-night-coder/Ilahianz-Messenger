package com.nightcoder.ilahianz.Models;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class UserData {
    private String username;
    private String id;
    private String imageURL;
    private String className;
    private String gender;
    private String nickname;
    private String category;
    private String search;
    private String status;
    private double lastSeen;
    private String email;
    private String birthday;
    private String birthYear;
    private String birthMonth;
    private String description;
    private String lastSeenPrivacy;
    private String locationPrivacy;
    private String phonePrivacy;
    private String emailPrivacy;
    private String profilePrivacy;
    private String phone;
    private String birthdayPrivacy;
    private String latitude;
    private String longitude;
    private String thumbnailURL;
    private String city;
    private String district;
    private String bio;
    private String department;
    private String idNumber;
    private String bloodDonate;
    private String academicYearFrom;
    private String academicYearTo;
    private String joinDate;
    private String bloodType;

    public UserData() {
    }

    public UserData(String username, String id, String imageURL, String className, String gender,
                    String nickname, String category, String search, String status,
                    double lastSeen, String email, String birthday,
                    String birthYear, String birthMonth,
                    String description, String lastSeenPrivacy,
                    String locationPrivacy, String phonePrivacy,
                    String emailPrivacy, String profilePrivacy,
                    String phone, String birthdayPrivacy,
                    String latitude, String longitude,
                    String thumbnailURL, String city,
                    String district, String bio,
                    String department, String idNumber,
                    String bloodDonate, String academicYearFrom,
                    String academicYearTo, String joinDate, String bloodType) {
        this.username = username;
        this.bloodType = bloodType;
        this.bloodDonate = bloodDonate;
        this.academicYearFrom = academicYearFrom;
        this.academicYearTo = academicYearTo;
        this.id = id;
        this.imageURL = imageURL;
        this.className = className;
        this.gender = gender;
        this.joinDate = joinDate;
        this.nickname = nickname;
        this.category = category;
        this.search = search;
        this.status = status;
        this.lastSeen = lastSeen;
        this.email = email;
        this.birthday = birthday;
        this.birthYear = birthYear;
        this.birthMonth = birthMonth;
        this.description = description;
        this.lastSeenPrivacy = lastSeenPrivacy;
        this.locationPrivacy = locationPrivacy;
        this.phonePrivacy = phonePrivacy;
        this.emailPrivacy = emailPrivacy;
        this.profilePrivacy = profilePrivacy;
        this.phone = phone;
        this.birthdayPrivacy = birthdayPrivacy;
        this.latitude = latitude;
        this.longitude = longitude;
        this.thumbnailURL = thumbnailURL;
        this.city = city;
        this.district = district;
        this.bio = bio;
        this.department = department;
        this.idNumber = idNumber;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }

    public String getAcademicYearFrom() {
        return academicYearFrom;
    }

    public void setAcademicYearFrom(String academicYearFrom) {
        this.academicYearFrom = academicYearFrom;
    }

    public String getAcademicYearTo() {
        return academicYearTo;
    }

    public void setAcademicYearTo(String academicYearTo) {
        this.academicYearTo = academicYearTo;
    }

    public String getBloodDonate() {
        return bloodDonate;
    }

    public void setBloodDonate(String bloodDonate) {
        this.bloodDonate = bloodDonate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(double lastSeen) {
        this.lastSeen = lastSeen;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(String birthYear) {
        this.birthYear = birthYear;
    }

    public String getBirthMonth() {
        return birthMonth;
    }

    public void setBirthMonth(String birthMonth) {
        this.birthMonth = birthMonth;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLastSeenPrivacy() {
        return lastSeenPrivacy;
    }

    public void setLastSeenPrivacy(String lastSeenPrivacy) {
        this.lastSeenPrivacy = lastSeenPrivacy;
    }

    public String getLocationPrivacy() {
        return locationPrivacy;
    }

    public void setLocationPrivacy(String locationPrivacy) {
        this.locationPrivacy = locationPrivacy;
    }

    public String getPhonePrivacy() {
        return phonePrivacy;
    }

    public void setPhonePrivacy(String phonePrivacy) {
        this.phonePrivacy = phonePrivacy;
    }

    public String getEmailPrivacy() {
        return emailPrivacy;
    }

    public void setEmailPrivacy(String emailPrivacy) {
        this.emailPrivacy = emailPrivacy;
    }

    public String getProfilePrivacy() {
        return profilePrivacy;
    }

    public void setProfilePrivacy(String profilePrivacy) {
        this.profilePrivacy = profilePrivacy;
    }

    public String getPhoneNumber() {
        return phone;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phone = phoneNumber;
    }

    public String getBirthdayPrivacy() {
        return birthdayPrivacy;
    }

    public void setBirthdayPrivacy(String birthdayPrivacy) {
        this.birthdayPrivacy = birthdayPrivacy;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
