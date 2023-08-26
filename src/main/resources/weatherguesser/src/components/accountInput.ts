import {styled} from "@mui/material";

export default styled('div')({
    padding: '1px',
    borderRadius: '5px',
    backgroundImage: 'linear-gradient(to right, #fca5f1, #fca5f1, #fbb0f4, #fbb0f4, #c5d9f5, #c5d9f5, #b2d7f9, #b2d7f9)',
    color: 'white',
    '& .MuiOutlinedInput-input': {
        padding: '4px',
        fontWeight: 'bold',
        width: '100%',
        height: '100%',
        color: 'white',
        '&::placeholder': {
            color: 'white',
        },
    },
    '& label': {
        fontWeight: 'bold',
        margin: '0',
        padding: '0',
        color: 'transparent'
    },
    '& label.Mui-focused': {
        color: '#b2d7f9'
    },
    '& .MuiInput-underline:after': {
        borderBottomColor: '#B2BAC2',
    },
    '& .MuiOutlinedInput-root': {
        '& fieldset': {
            borderColor: 'white',
        },
        '&:hover fieldset': {
            borderColor: '#b2d7f9',
        },
        '&.Mui-focused fieldset': {
            borderColor: '#fca5f1',
        },
    },
});