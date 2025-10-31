package rehber;


public class Kisi {
    private String ad,soyad,telefon,email,adres,fotoYolu;
    int id,grupId;
    private String ec2FotoUrl;

    public Kisi(int id,String ad, String soyad, String telefon, String email, String adres,String fotoYolu,int grupId) {
        this.id = id;
        this.ad = ad;
        this.soyad = soyad;
        this.telefon = telefon;
        this.email = email;
        this.adres = adres;
        this.fotoYolu=fotoYolu;
        this.grupId=grupId;
    }

    public String getec2FotoUrl() {return ec2FotoUrl;}

    public int getId(){return  id;}

    public String getAd() {
        return ad;
    }

    public String getSoyad() {
        return soyad;
    }

    public String getTelefon() {
        return telefon;
    }

    public String getEmail() {
        return email;
    }

    public String getAdres() {return adres;}

    public String getFotoYolu() { return fotoYolu; }

    public int getGrupId() {return grupId;}

    public void setId(int id) {this.id=id;}
    public void setAd(String ad) { this.ad = ad; }
    public void setSoyad(String soyad) { this.soyad = soyad; }
    public void setTelefon(String telefon) { this.telefon = telefon; }
    public void setEmail(String email) { this.email = email; }
    public void setAdres(String adres) { this.adres = adres; }
    public void setFotoYolu(String fotoYolu) { this.fotoYolu = fotoYolu; }
    public void setGrupId(int grupId){this.grupId = grupId;}
    public void setEc2FotoUrl(String ec2FotoUrl) {this.ec2FotoUrl = ec2FotoUrl;}

    @Override
    public String toString() {
        return (ad+" "+soyad);
    }

}