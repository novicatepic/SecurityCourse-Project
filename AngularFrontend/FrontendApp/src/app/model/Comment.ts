import { User } from "./User";


export class Comment {

    public id?:number;
    public writer?:User;

    constructor(public title: string, 
        public content: string, public roomId: number,
        public userId:number, public enabled: boolean, public forbidden: boolean,
        public dateCreated: Date) {
        
    }
}