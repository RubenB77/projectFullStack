export class User {
    userId?: number;
    username?: string;
    password?: string;
    email?: string;
    role?: string;
    createdAt?: Date;
    updatedAt?: Date;

    constructor(data: Partial<User>) {
        Object.assign(this, data);
    }

    get getUserId(): string {
        return this.userId !== undefined ? this.userId.toString() : '';
    }

    get getUserPassword(): string {
        return this.password !== undefined ? this.password.toString() : '';
    }
}
