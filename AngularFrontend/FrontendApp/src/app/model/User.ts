

export class User {
    id?: number;
    active: boolean = false;
    isTerminated: boolean = false;
    role: any = "ROLE_UNDEFINED";
    

    constructor(public username?: string, public password?:string, public email?:string) {

    }

}