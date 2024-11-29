export class Address {
    addressName: string;
    postCode: number;
    city: string;
    latitude: number;
    longitude: number;
    

    constructor(addressName: string, postCode: number, city: string, latitude: number, longitude: number) {
        this.addressName = addressName;
        this.postCode = postCode;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}