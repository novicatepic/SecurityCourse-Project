

export class User {
    /*username : string;
    password: string;
    email: string;*/
    active: boolean = false;
    createEnabled: boolean = false;
    deleteEnabled: boolean = false;
    readEnabled: boolean = false;
    updateEnabled: boolean = false;
    isTerminated: boolean = false;
    role: any = "ROLE_UNDEFINED";

    constructor(private username: string, private password:string, private email:string) {

    }

}