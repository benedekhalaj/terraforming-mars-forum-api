import { Injectable } from '@angular/core';
import {InMemoryDbService} from "angular-in-memory-web-api";
import {User} from "../interfaces/user";
import {Comment} from "../interfaces/comment";

@Injectable({
  providedIn: 'root'
})
export class InMemoryDataService implements InMemoryDbService{

  constructor() { }

  createDb() {
    const users: User[] = [
      {
        id: 1,
        email: "bende@gmail.com",
        firstName: "Hello",
        lastName: "user",
        imageSource: "https://xsgames.co/randomusers/assets/images/favicon.png",
        username: "bende",
        password: "1234"
      }
    ]
    const comments: Comment[] = [
      {
        id: 1,
        user: users[0],
        description: "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut" +
          "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut" +
          "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum" +
          "dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia" +
          "deserunt mollit anim id est laborum.",
        timestamp: new Date()
      }
    ]
    return {users, comments};
  }
}