import {styled} from "@mui/material";

export default styled('div')({
    padding: '1px',
    borderRadius: '5px',
    backgroundImage: 'linear-gradient(to right, rgba(245, 245, 245, 1), rgba(45, 130, 178, 1))',
    color: 'white',
    '& .MuiOutlinedInput-input': {
        padding: '4px',
        fontWeight: 'bold',
        width: '100%',
        height: '100%',
        color: 'white',textAlign: 'right',
        '&::placeholder': {
            color: 'white',
            textAlign: 'right',
        },
    },
    '& label': {
        fontWeight: 'bold',
        margin: '0',
        padding: '0',
        color: 'transparent'
    },
    '& label.Mui-focused': {
        color: 'rgba(45, 130, 178, 1)'
    },
    '& .MuiInput-underline:after': {
        borderBottomColor: '#B2BAC2',
    },
    '& .MuiOutlinedInput-root': {
        '& fieldset': {
            borderColor: 'white',
        },
        '&:hover fieldset': {
            borderColor: 'rgba(245, 245, 245, 1)',
        },
        '&.Mui-focused fieldset': {
            borderColor: 'rgba(45, 130, 178, 1)',
        },
    },
});