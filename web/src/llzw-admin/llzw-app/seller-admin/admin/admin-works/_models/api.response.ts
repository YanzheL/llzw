export class ApiResponse {
    responseId: string;
    timestamp: string;
    status: number;
    success: boolean = false;
    data: any;
    error: string;
}
