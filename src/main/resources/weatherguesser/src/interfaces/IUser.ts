export default interface user {
    name: string;
    password: string;
    location: string;
    accuracy: number;
    totalGuesses: number;
    registration: Date;
    rating : number;
    currentStreak: number;
    highStreak: number;
    email: string;
    userId: string;
    guess: string[];
}
