export interface Response {
    status: 'success' | 'fail' | 'error',
    message: string,
    data: any
}